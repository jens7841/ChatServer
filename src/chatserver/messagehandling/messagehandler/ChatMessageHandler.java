package chatserver.messagehandling.messagehandler;

import java.io.UnsupportedEncodingException;

import chatserver.messagehandling.Message;
import chatserver.messagehandling.MessageType;
import chatserver.server.UserHandler;
import chatserver.usermanagement.User;
import chatserver.usermanagement.UserManager;

public class ChatMessageHandler implements MessageHandler {

	private UserManager userManager;

	public ChatMessageHandler(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public void handleMessage(Message message, UserHandler userHandler) {
		try {
			User user = userHandler.getUser();
			if (user != null) {
				System.out.println("> " + user.getName() + ": " + new String(message.getBytes(), "UTF-8"));
				userManager.sendToAllUsers(
						new Message(("> " + user.getName() + ": " + message.toString()).getBytes("UTF-8"),
								MessageType.CHAT_MESSAGE),
						user);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
