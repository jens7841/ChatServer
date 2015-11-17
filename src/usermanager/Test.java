package usermanager;

import java.io.FileOutputStream;

public class Test {

	public static void main(String[] args) throws Exception {
		Usermanager mgr = new Usermanager("users.csv");
		mgr.userRegistration("Test1", "TestPw1");
		mgr.userRegistration("Test2", "TestPw2");
		mgr.userRegistration("Test3", "TestPw3");
		mgr.userRegistration("Test4", "TestPw4");

		mgr.writeUserData(new FileOutputStream("users.csv"));
	}
}
