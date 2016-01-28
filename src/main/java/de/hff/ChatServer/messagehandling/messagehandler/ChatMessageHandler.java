package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import de.hff.ChatServer.usermanagement.User;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatServer.usermanagement.UserManager;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class ChatMessageHandler implements MessageHandler {

	private UserManager userManager;
	private UserHandler userHandler;

	public ChatMessageHandler(UserManager userManager, UserHandler usHandler) {
		this.userManager = userManager;
		this.userHandler = usHandler;
	}

	@Override
	public void receiveMessage(Message message) {
		if (message.getType().equals(MessageType.CHAT_MESSAGE)) {
			handleMessage(message);
		}
	}

	@Override
	public void handleMessage(Message message) {
		try {
			User user = userHandler.getUser();
			if (user != null) {
				System.out.println("> " + user.getName() + ": " + new String(message.getBytes(), "UTF-8"));
				try {

					userManager.sendToAllUsers(
							new Message(("> " + user.getName() + ": " + message.toString()).getBytes("UTF-8"),
									MessageType.CHAT_MESSAGE),
							user, user);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
