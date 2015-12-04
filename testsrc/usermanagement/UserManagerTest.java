package usermanagement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import messagehandling.Connection;

public class UserManagerTest {

	private static boolean setupDone = false;
	private static boolean registrationIsDone = false;
	private static UserManager manager;

	@Before
	public void setUp() {
		if (!setupDone) {

			manager = new UserManager("usersTest.csv");

			manager.clearUserList();

			setupDone = true;
		}
	}

	@Test
	@Before
	public void testRegistration() {
		if (setupDone && !registrationIsDone) {

			manager.addUser("Rolf", "WasGehtSieDasAn");

			manager.addUser("Harald", "WasGehtSieDasAn1");

			manager.addUser("Hans", "WasGehtSieDasAn2");

			manager.login(manager.getUser("rolf"), new Connection(null), null);
			registrationIsDone = true;
		}
	}

	@Test
	public void testUserNextID() {
		manager.addUser("Clemens", "cl123");
		Assert.assertEquals(4, manager.getNextID());

	}

	@Test
	public void testLogin() {

		manager.login(manager.getUser("harald"), new Connection(null), null);

		Assert.assertEquals(true, manager.isUserOnline(manager.getUser("harald")));
	}

	@Test
	public void testLogout() {

		manager.logout(manager.getUser("rolF"));

		Assert.assertEquals(false, manager.isUserOnline(manager.getUser("rolf")));
	}

	@Test
	public void testLoadingDataFromFile() {
		UserManager userManager = new UserManager("usersTest.csv");
		Assert.assertNotEquals(null, userManager.getUser("rolf"));
	}
}