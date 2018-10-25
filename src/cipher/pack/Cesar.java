<<<<<<< HEAD
package cipher.pack;

public class Cesar {
	private int key=4;
	
	public String decrypt(String cipher) {		
		String plaintext="";
		int i=0;
		while(i < cipher.length()) {
			char c = cipher.charAt(i);
			int asciCode = (int)c-this.key;
			c=(char)asciCode;
			plaintext+=c;
			i++;
		}
		return plaintext;
	}
	
	public String encrypt(String plaintext) {	
		String cipher="";
		int i=0;
		while(i < plaintext.length()) {
			char c = plaintext.charAt(i);
			int asciCode = (int)c+this.key;
			c=(char)asciCode;
			cipher+=c;
			i++;
		}
		return cipher;
	}
}
=======
package cipher.pack;

public class Cesar {
	private int key=4;
	
	public String decrypt(String cipher) {		
		String plaintext="";
		int i=0;
		while(i < cipher.length()) {
			char c = cipher.charAt(i);
			int asciCode = (int)c-this.key;
			c=(char)asciCode;
			plaintext+=c;
			i++;
		}
		return plaintext;
	}
	
	public String encrypt(String plaintext) {	
		String cipher="";
		int i=0;
		while(i < plaintext.length()) {
			char c = plaintext.charAt(i);
			int asciCode = (int)c+this.key;
			c=(char)asciCode;
			cipher+=c;
			i++;
		}
		return cipher;
	}
}
>>>>>>> origin/version-1.0.1
