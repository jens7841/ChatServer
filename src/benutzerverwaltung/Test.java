package benutzerverwaltung;

import java.io.FileOutputStream;

public class Test {

	public static void main(String[] args) throws Exception {
		Benutzerverwaltung ver = new Benutzerverwaltung("users.csv");
		ver.benutzerRegistrieren("Test3", "TestPw1");

		ver.schreibeUserDaten(new FileOutputStream("users.csv"));
	}
}
