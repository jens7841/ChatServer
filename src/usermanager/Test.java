package usermanager;

public class Test {

	public static void main(String[] args) throws Exception {
		Usermanager mgr = new Usermanager("users.csv");
		mgr.userRegistration("Test1", "TestPw1");

		mgr.userLogin("Test1", "TestPw1", null);

	}
}
