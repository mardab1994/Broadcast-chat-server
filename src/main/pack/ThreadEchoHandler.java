package main.pack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class ThreadEchoHandler implements Runnable {

	// flags of cipher mode
	private final int CIPHER_MODE_NONE 	= 0;
	private final int CIPHER_MODE_XOR   = 1;
	private final int CIPHER_MODE_CESAR = 2;
	
	private static int counter = 0;				//client counter
	private static int actualCounter = counter; // actual client counter 
	private int clientId = 0;					// client id
	private static Vector<Socket> myClients;			// vector of clients
	private static Vector<Boolean>stateOfClients;		// vector of state of clients (is he active or not)
	private static Vector<Integer>clientsCipherModes;	// vector of clients cipher modes
		 
	public ThreadEchoHandler(Socket i) {
		Socket incoming = i;
		if(counter == 0) {
			myClients = new Vector<Socket>();
			stateOfClients = new Vector<Boolean>();
		}
		myClients.add(incoming);
		stateOfClients.add(true);
		
		this.clientId = counter;
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
