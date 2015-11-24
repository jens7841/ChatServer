package commands;

import java.io.IOException;

import usermanager.User;

public class Logout extends Command {

	public Logout(String command, String... aliases) {
		super(command, aliases);
	}

	@Override
	public void execute(User user) {
		try {
			System.out.println("hi");
			user.logout();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
