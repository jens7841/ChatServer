package commands;

import usermanagement.User;

public class Logout extends Command {

	public Logout(String command, String... aliases) {
		super(command, aliases);
	}

	@Override
	public void execute(User user) {

	}

}
