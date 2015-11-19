package usermanager;

public class Test {

	public static void main(String[] args) throws Exception {
		UserManager mgr = new UserManager("users.csv");
		mgr.registerUser("Test1", "TestPw1");

		mgr.loginUser("Test1", "TestPw1", null);

	}
}
