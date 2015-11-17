package benutzerverwaltung;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Verschluesselung {
	public static void main(String args[]) throws Exception {
		String s = "This is a test";
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(), 0, s.length());
		System.out.println("MD5: " + new BigInteger(1, m.digest()).toString(16));
	}
}
