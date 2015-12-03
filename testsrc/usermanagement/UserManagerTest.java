package usermanagement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import messagehandling.Connection;

public class UserManagerTest {

	private boolean setupDone = false;
	private UserManager manager;

	@Before
	public void setUp() {
		if (!setupDone) {
			manager = new UserManager("usersTest.csv");

			manager.addUser(new User("Rolf", "WasGehtSieDasAn", 0));
			manager.addUser(new User("Harald", "WasGehtSieDasAn1", 1));
			manager.addUser(new User("Hans", "WasGehtSieDasAn2", 2));

			manager.login(manager.getUser("rolf"), new Connection(null));

		}
	}

	@Test
	public void testLogin() {
		manager.login(manager.getUser("harald"), null);

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