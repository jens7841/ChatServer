package chatserver.messagehandling.messagehandler;

import chatserver.messagehandling.Message;
import chatserver.server.UserHandler;
import chatserver.usermanagement.User;
import chatserver.usermanagement.UserManager;

public class DisconnectMessageHandler implements MessageHandler {

	private UserManager userManager;

	public DisconnectMessageHandler(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public void handleMessage(Message message, UserHandler userHandler) {
		User user = userHandler.getUser();
		if (user != null) {
			userManager.logout(user);
		} else {
			System.out.println("Eine Verbindung wurde getrennt");
		}
	}

}
