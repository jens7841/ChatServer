package messagehandling;

import java.io.IOException;

import chatshared.Messages;
import server.Connection;
import usermanagement.User;
import usermanagement.UserAlreadyExistsException;
import usermanagement.UserException;
import usermanagement.UserManager;

public class MessageHandler {

	private User user;
	private Connection connection;
	private UserManager userManager;

	public MessageHandler(Connection connection, UserManager userManager) {
		this.userManager = userManager;
		this.connection = connection;
	}

	public void loginMessage(Message message) {
		if (user == null) {

			String[] split = message.toString().split("\\" + (char) ((byte) Messages.DELIMITER));
			if (split.length >= 2) {

				String username = split[0];
				String password = split[1];

				try {
					userManager.registerUser(username, password);

					connection.sendMessage(new Message("Erfolgreich Registriert!", MessageType.SUCCESS_MESSAGE));

					user = userManager.loginUser(username, password, connection);

					connection.sendMessage(new Message("Erfolgreich eingeloggt!", MessageType.LOGIN_SUCCESS_MESSAGE));

				} catch (UserAlreadyExistsException e) {

					try {
						user = userManager.loginUser(username, password, connection);
						connection
								.sendMessage(new Message("Erfolgreich eingeloggt!", MessageType.LOGIN_SUCCESS_MESSAGE));

					} catch (UserAlreadyExistsException e1) {
						connection.sendMessage(
								new Message("Der User ist bereits eingeloggt!", MessageType.LOGIN_ERROR_MESSAGE));
					} catch (UserException e1) {
						connection.sendMessage(
								new Message("Das eingegebene Passwort ist falsch!", MessageType.LOGIN_ERROR_MESSAGE));
					}

				} catch (UserException e) {
					connection.sendMessage(new Message(e.getMessage(), MessageType.LOGIN_ERROR_MESSAGE));
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				connection.sendMessage(new Message("LoginMessageFehler!", MessageType.LOGIN_ERROR_MESSAGE));
			}
		}
	}

	public void chatMessage(Message message) {
		if (user == null) {
			connection.sendMessage(new Message("Du bist nicht eingeloggt!", MessageType.LOGIN_ERROR_MESSAGE));
		} else {

			Message toAllMessage = new Message(user.getName() + ": " + message.toString(), MessageType.CHAT_MESSAGE);
			userManager.sendToAllUsers(toAllMessage, user);

			System.out.println("-> " + toAllMessage);

		}
	}

}
