package benutzerverwaltung;

import java.io.FileOutputStream;

import usermanager.Usermanager;

public class Test {

	public static void main(String[] args) throws Exception {
		Usermanager ver = new Usermanager("users.csv");
		ver.userRegistration("Test3", "TestPw1");

		ver.writeUserData(new FileOutputStream("users.csv"));
	}
}
