package messagehandling;

import java.io.UnsupportedEncodingException;

import usermanagement.User;
import usermanagement.UserManager;

public class ServerMessageHandler implements MessageHandler {

	private UserManager userManager;

	public ServerMessageHandler(UserManager userManager) {
		this.userManager = userManager;

	}

	@Override
	public void handleMessage(Message message, Connection connection) {

		switch (message.getType()) {
		case CHAT_MESSAGE:
			chatMessage(message, connection);
			break;

		default:
			break;
		}

	}

	private void chatMessage(Message message, Connection connection) {

		User user = userManager.getUser(connection);

		if (user != null && userManager.isUserOnline(user)) {
			try {
				userManager.sendToAllUsers(new Message(user.getName() + ": " + new String(message.getBytes(), "UTF-8"),
						MessageType.CHAT_MESSAGE));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

	}

}
