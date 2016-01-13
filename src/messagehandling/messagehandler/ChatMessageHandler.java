package messagehandling.messagehandler;

import java.io.UnsupportedEncodingException;

import messagehandling.Message;
import messagehandling.MessageType;
import server.UserHandler;
import usermanagement.User;
import usermanagement.UserManager;

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
