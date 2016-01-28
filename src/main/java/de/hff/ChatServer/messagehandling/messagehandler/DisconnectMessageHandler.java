package de.hff.ChatServer.messagehandling.messagehandler;

import de.hff.ChatServer.usermanagement.User;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatServer.usermanagement.UserManager;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class DisconnectMessageHandler implements MessageHandler {

	private UserManager userManager;
	private UserHandler userHandler;

	public DisconnectMessageHandler(UserManager userManager, UserHandler userHandler) {
		this.userManager = userManager;
		this.userHandler = userHandler;
	}

	@Override
	public void receiveMessage(Message message) {
		if (message.getType() == MessageType.DISCONNECT) {
			handleMessage(message);
		}
	}

	@Override
	public void handleMessage(Message message) {
		User user = userHandler.getUser();
		if (user != null) {
			userManager.logout(user);
		} else {
			System.out.println("Eine Verbindung wurde getrennt");
		}
	}

}
