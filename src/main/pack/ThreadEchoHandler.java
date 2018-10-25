package main.pack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class ThreadEchoHandler implements Runnable {
	Random generator = new Random(); 
	
	// flags of cipher mode
	private final int CIPHER_MODE_NONE 	= 0;
	private final int CIPHER_MODE_XOR   = 1;
	private final int CIPHER_MODE_CESAR = 2;
	
	private static int counter = 0;						// client counter
	private static int actualCounter = counter; 		// actual client counter 
	private int clientId = 0;							// client id
	private static Vector<Socket> myClients;			// vector of clients
	private static Vector<Boolean>stateOfClients;		// vector of state of clients (is he active or not)
	private static Vector<Integer>clientsCipherModes;	// vector of clients cipher modes
	
	private static Vector<Integer>vectorOf_P;					// vector of P numbers for every session
	private static Vector<Integer>vectorOf_G;					// vector of G numbers for every session
	private static Vector<Integer>vectorOfServerSecret_A;		// vector of secret a number for every session
	private static Vector<Integer>vectorOf_A_2send2Client;		// vector of S numbers to send to clients
	private static Vector<Integer>vectorOfSessionKeys;			// vector of session keys
	private static Vector<Integer>vectorOf_B_fromClient;		// vector of B numbers from client
	
	
	public ThreadEchoHandler(Socket i) {
		Socket incoming = i;
		if(counter == 0) {								// init all vectors
			myClients = new Vector<Socket>();
			stateOfClients = new Vector<Boolean>();
			clientsCipherModes = new Vector<Integer>();
			
			vectorOf_P = new Vector<Integer>();
			vectorOf_G = new Vector<Integer>();
			vectorOfSessionKeys = new Vector<Integer>();
			vectorOfServerSecret_A = new Vector<Integer>();
			vectorOf_A_2send2Client = new Vector<Integer>();
			vectorOf_B_fromClient = new Vector<Integer>();
		}
		myClients.add(incoming);
		stateOfClients.add(true);
		
		int randomNumber = generator.nextInt(24) + 3;	// random P number
		vectorOf_P.add(randomNumber);
		
		randomNumber = generator.nextInt(20) + 3;		// random G number
		vectorOf_G.add(randomNumber);
	
		this.clientId = counter;
		
		randomNumber = generator.nextInt(10) + 3;		// random secret A number
		vectorOfServerSecret_A.add(randomNumber);
		Long tmp = ((long) Math.pow(vectorOf_G.get(this.clientId), vectorOfServerSecret_A.get(this.clientId)));
		
		vectorOf_A_2send2Client.add((int)(tmp%vectorOf_P.get(this.clientId)));	// calculate A number to send to client
		
		actualCounter = counter;
		counter++;
	}

	@Override
	public void run() {
		try {
			try {
				InputStream inStream = myClients.get(clientId).getInputStream();
				OutputStream outStream = myClients.get(clientId).getOutputStream();
				
				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);
				
				out.println(Integer.toString(this.clientId));					// send client id
				out.println(Integer.toString(vectorOf_P.get(this.clientId)));	// send P to client
				out.println(Integer.toString(vectorOf_G.get(this.clientId)));	// send G to client
				out.println(Integer.toString(vectorOf_A_2send2Client.get(this.clientId)));	//send A number to client
				
				if(in.hasNextLine()) {
					vectorOf_B_fromClient.add(Integer.parseInt(in.nextLine()));		//get B number from client
				}
				System.out.println("S E R V E R ");
				System.out.println("P = "+vectorOf_P.get(this.clientId));			
				System.out.println("G = "+vectorOf_G.get(this.clientId));
				System.out.println("Secter a = "+vectorOfServerSecret_A.get(this.clientId));
				System.out.println("A send to client = "+vectorOf_A_2send2Client.get(this.clientId));
				System.out.println("B from client = "+vectorOf_B_fromClient.get(this.clientId));
				
				vectorOfSessionKeys.add((int)(Math.pow(vectorOf_B_fromClient.get(this.clientId),vectorOfServerSecret_A.get(this.clientId)) % vectorOf_P.get(this.clientId))); 
				System.out.println("Klucze sesji obliczony na serwerze = "+vectorOfSessionKeys.get(this.clientId));
				out.println(Integer.toString(vectorOfSessionKeys.get(this.clientId)));	//send calculated session key to client
				
				if(in.hasNextLine()) {
					clientsCipherModes.add(Integer.parseInt(in.nextLine()));
					System.out.println(this.clientId+" cipher mode = "+clientsCipherModes.get(this.clientId));
				}
				out.println("Hello you are Client numer "+(counter-1)+"\n\n");
				System.out.println("My client id = "+this.clientId);
				boolean done = false;
				
				while(!done && in.hasNextLine()) {
					String line = in.nextLine();
					//System.out.println("Counter ="+counter);
					for(int i = 0; i < stateOfClients.size(); i++) {
						if(stateOfClients.get(i)==true) {
							//System.out.println("i = "+i);
							if(i!=this.clientId) {
								OutputStream multiOutStream = myClients.get(i).getOutputStream();
								PrintWriter multiOut = new PrintWriter(multiOutStream, true);
								multiOut.println("Client "+this.clientId+" "+line);//client -> name/nick
							}
						}
					}
					if(line.trim().equals("BYE")) {
						done = true;
					}
				}
				
			}finally {
				myClients.get(this.clientId).close();
				//myClients.remove(this.clientId);
				stateOfClients.set(clientId, false);
				actualCounter--;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
