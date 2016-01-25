package de.hff.ChatServer.messagehandling.messagehandler;

import de.hff.ChatServer.usermanagement.User;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatServer.usermanagement.UserManager;
import de.hff.ChatShared.messagehandling.Message;

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
