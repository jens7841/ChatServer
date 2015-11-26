package server;

import chatshared.Messages;
import usermanager.User;
import usermanager.UserAlreadyExistsException;
import usermanager.UserAlreadyLoggedInException;
import usermanager.UserException;
import usermanager.UserManager;

public class MessageHandler {

	private User user;
	private UserManager userManager;
	private FileManager fileManager;
	private CommandHandler commandHandler;

	public MessageHandler() {

	}

	public void login(String message) {
		String[] split = message.split("\\" + (char) ((byte) Messages.DELIMITER));
		try {
			if (split.length < 2) {
				new MessageSender(user).sendMessage("LoginMessageFehler", Messages.LOGIN_ERROR_MESSAGE);
			} else {
				user = userManager.loginUser(split[0], split[1], socket);
				new MessageSender(user).sendMessage("Login Erfolgreich", Messages.LOGIN_SUCCESS_MESSAGE);

			}

		} catch (UserAlreadyLoggedInException e1) {
			new MessageSender(user).sendMessage("User ist bereits eingeloggt!", Messages.LOGIN_ERROR_MESSAGE);

		} catch (UserException e) {
			try {
				userManager.registerUser(split[0], split[1]);
				new MessageSender(user).sendMessage("Registrierung erfolgreich!", Messages.SUCCESS_MESSAGE);

				try {
					user = userManager.loginUser(split[0], split[1], user.getSocket());
					new MessageSender(user).sendMessage("Login Erfolgreich", Messages.LOGIN_SUCCESS_MESSAGE);
				} catch (UserException e1) {
					new MessageSender(user).sendMessage("Fehler beim einloggen nach dem Registrieren!",
							Messages.ERROR_MESSAGE);
				}

			} catch (UserAlreadyExistsException e1) {
				new MessageSender(user).sendMessage("Das eingegebene Passwort ist falsch!",
						Messages.LOGIN_ERROR_MESSAGE);
			} catch (UserException e2) {
				new MessageSender(user).sendMessage(e2.getMessage(), Messages.LOGIN_ERROR_MESSAGE);
			}
		}
	}

}
