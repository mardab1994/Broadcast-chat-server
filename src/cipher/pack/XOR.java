<<<<<<< HEAD
package cipher.pack;

public class XOR {
	private String key="KCQDA";
	public XOR() {}

	public String encryptDecrypt(String input) {
		StringBuilder output = new StringBuilder();	
		for(int i = 0; i < input.length(); i++) {
			output.append((char) (input.charAt(i) ^ key.charAt(i % key.length())));
		}
		return output.toString();
	}
}
=======
package cipher.pack;

public class XOR {
	private String key="KCQDA";
	public XOR() {}

	public String encryptDecrypt(String input) {
		StringBuilder output = new StringBuilder();	
		for(int i = 0; i < input.length(); i++) {
			output.append((char) (input.charAt(i) ^ key.charAt(i % key.length())));
		}
		return output.toString();
	}
}
>>>>>>> origin/version-1.0.1
