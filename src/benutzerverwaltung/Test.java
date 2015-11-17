package benutzerverwaltung;

import java.io.FileOutputStream;

public class Test {

	public static void main(String[] args) throws Exception {
		Benutzerverwaltung ver = new Benutzerverwaltung("users.csv");
		ver.benutzerRegistrieren("Test1", "TestPw1");
		ver.benutzerRegistrieren("Test2", "TestPw2");
		ver.benutzerRegistrieren("Test3", "TestPw3");
		ver.benutzerRegistrieren("Test4", "TestPw4");

		ver.schreibeUserDaten(new FileOutputStream("users.csv"));
	}
}
