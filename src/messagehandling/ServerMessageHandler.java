package messagehandling;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
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
		case LOGIN:
			loginMessage(message, connection);
			break;
		default:
			break;
		}

	}

	private void loginMessage(Message message, Connection connection) {
		User user = userManager.getUser(connection);

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getBytes()));

		try {
			byte[] usernameBytes = new byte[in.readInt()];
			in.readFully(usernameBytes);
			String username = new String(usernameBytes, "UTF-8");

			byte[] passwordBytes = new byte[in.readInt()];
			in.readFully(passwordBytes);
			String password = new String(passwordBytes, "UTF-8");

			MultiThreadedMessageSender messageSender = new MultiThreadedMessageSender(connection);

			if (user == null) {
				userManager.addUser(username, password);

				messageSender.sendMessage(new Message("Erfolgreich registriert!", MessageType.SUCCESS_MESSAGE));

				userManager.login(userManager.getUser(username), connection, messageSender);

				messageSender.sendMessage(new Message("Erfolgreich eingeloggt!", MessageType.LOGIN_SUCCESS_MESSAGE));
			} else {
				if (userManager.getUser(user.getName()).getPassword().equals(password)) {
					if (!userManager.isUserOnline(user)) {
						userManager.login(user, connection, messageSender);

						messageSender
								.sendMessage(new Message("Erfolgreich eingeloggt.", MessageType.LOGIN_SUCCESS_MESSAGE));
					} else {
						messageSender.sendMessage(
								new Message("User ist bereits eingeloggt!", MessageType.LOGIN_ERROR_MESSAGE));
					}
				} else {
					messageSender.sendMessage(new Message("Falsches Passwort!", MessageType.LOGIN_ERROR_MESSAGE));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
