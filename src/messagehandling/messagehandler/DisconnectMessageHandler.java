package messagehandling.messagehandler;

import messagehandling.Message;
import server.UserHandler;
import usermanagement.User;
import usermanagement.UserManager;

public class DisconnectMessageHandler implements MessageHandler {

	private UserManager userManager;

	public DisconnectMessageHandler(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public void handleMessage(Message message, UserHandler connectionHandler) {
		User user = connectionHandler.getUser();
		if (user != null) {
			userManager.logout(user);
		} else {
			System.out.println("Eine Verbindung wurde getrennt");
		}
	}

}
