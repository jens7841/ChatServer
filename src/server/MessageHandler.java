package server;

import chatshared.Messages;
import usermanager.UserAlreadyExistsException;
import usermanager.UserAlreadyLoggedInException;
import usermanager.UserException;

public class MessageHandler {

	public MessageHandler() {

	}

	public void login(String message) {
		String[] split = message.split("\\" + (char) ((byte) Messages.DELIMITER));
		try {
			if (split.length < 2) {
				new MessageSender(socket).sendMessage("LoginMessageFehler", Messages.LOGIN_ERROR_MESSAGE);
			} else {
				user = userManager.loginUser(split[0], split[1], socket);
				new MessageSender(socket).sendMessage("Login Erfolgreich", Messages.LOGIN_SUCCESS_MESSAGE);

			}

		} catch (UserAlreadyLoggedInException e1) {
			new MessageSender(socket).sendMessage("User ist bereits eingeloggt!", Messages.LOGIN_ERROR_MESSAGE);

		} catch (UserException e) {
			try {
				userManager.registerUser(split[0], split[1]);
				new MessageSender(socket).sendMessage("Registrierung erfolgreich!", Messages.SUCCESS_MESSAGE);

				try {
					user = userManager.loginUser(split[0], split[1], socket);
					new MessageSender(socket).sendMessage("Login Erfolgreich", Messages.LOGIN_SUCCESS_MESSAGE);
				} catch (UserException e1) {
					new MessageSender(socket).sendMessage("Fehler beim einloggen nach dem Registrieren!",
							Messages.ERROR_MESSAGE);
				}

			} catch (UserAlreadyExistsException e1) {
				new MessageSender(socket).sendMessage("Das eingegebene Passwort ist falsch!",
						Messages.LOGIN_ERROR_MESSAGE);
			} catch (UserException e2) {
				new MessageSender(socket).sendMessage(e2.getMessage(), Messages.LOGIN_ERROR_MESSAGE);
			}
		}
	}

}
