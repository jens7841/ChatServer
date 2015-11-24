package commands;

import chatshared.Messages;
import server.MessageSender;
import usermanager.User;

public class Logout extends Command {

	public Logout(String command, String... aliases) {
		super(command, aliases);
	}

	@Override
	public void execute(User user) {
		new MessageSender(user.getSocket()).sendMessage("Disconnected!", Messages.DISCONNECT);
	}

}
