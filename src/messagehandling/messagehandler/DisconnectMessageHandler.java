package messagehandling.messagehandler;

import messagehandling.Message;
import server.ConnectionHandler;
import usermanagement.User;
import usermanagement.UserManager;

public class DisconnectMessageHandler implements MessageHandler {

	private UserManager userManager;

	public DisconnectMessageHandler(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public void handleMessage(Message message, ConnectionHandler connectionHandler) {
		User user = userManager.getUser(connectionHandler);
		userManager.logout(user);
		System.out.println("Der Benutzer " + user.getName() + " hat sich ausgeloggt!");
	}

}
