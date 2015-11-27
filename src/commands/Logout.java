package commands;

import messagehandling.Message;
import messagehandling.MessageType;
import usermanagement.User;

public class Logout extends Command {

	public Logout(String command, String... aliases) {
		super(command, aliases);
	}

	@Override
	public void execute(User user, String input) {
		user.getConnection().sendMessage(new Message("", MessageType.DISCONNECT));
	}
}
