package de.hff.ChatServer.usermanagement;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class UserManagerTest {

	private String fileName = "usertest.csv";
	private File file = new File(fileName);
	private UserManager userManager = new UserManager(fileName);
	private String userName = "TestUser";

	@Test
	public void testRegistration() throws Throwable, IOException {

		userManager.createEmptyUserFile(file);

		userManager.registerUser(userName, "asdfjklö");

		try {
			userManager.registerUser(userName, "ölkjfdsa");
			fail("UserAlreadyExistsException should be thrown");
		} catch (UserAlreadyExistsException e) {
		}

	}

	@Test
	public void testLoginAndRegistration() throws Throwable {

		userManager.createEmptyUserFile(file);

		String password = "DasIstEinSuperTollesUndSicheresPasswort123!BÄM";
		userManager.registerUser(userName, password);

		User user = userManager.loginUser(userName, password, null, null, null);

		try {
			userManager.loginUser(userName, password, null, null, null);
			fail("UserAlreadyLoggedInExcpetion should be thrown");
		} catch (UserAlreadyLoggedInException e) {
		}

		Assert.assertEquals(true, userManager.isUserOnline(user));

	}

	@Test
	public void testLogout() throws Throwable {

		userManager.createEmptyUserFile(file);

		String password = "DasIstEinSuperTollesUndSicheresPasswort123!BÄM";
		userManager.registerUser(userName, password);

		User user = userManager.loginUser(userName, password, null, null, null);

		userManager.logout(user);

		Assert.assertEquals(false, userManager.isUserOnline(user));

	}

}