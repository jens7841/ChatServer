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
		User user = connectionHandler.getUser();
		if (user != null) {
			userManager.logout(user);
			System.out.println("Der Benutzer " + user.getName() + " hat sich ausgeloggt!");
		} else {
			System.out.println("Eine Verbindung wurde getrennt");
		}
	}

}
