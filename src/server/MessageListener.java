package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import chatshared.Messages;
import usermanager.User;
import usermanager.UserAlreadyExistsException;
import usermanager.UserAlreadyLoggedInException;
import usermanager.UserException;
import usermanager.UserManager;

public class MessageListener extends Thread {

	private Socket socket;
	private User user;
	private UserManager userManager;

	public MessageListener(Socket socket, UserManager usermanager) {
		this.socket = socket;
		this.userManager = usermanager;
	}

	@Override
	public void run() {

		while (!socket.isClosed()) {

			try {
				InputStream in = new BufferedInputStream(socket.getInputStream());
				int messageType = in.read();

				StringBuilder builder = new StringBuilder();
				int read;
				while ((read = in.read()) != Messages.END_OF_MESSAGE) {
					builder.append((char) read);
				}

				switch (messageType) {
				case Messages.CHAT_MESSAGE:
					if (user != null) {
						for (User u : userManager.getUserList()) {

							if (u.isOnline() && !u.equals(user)) {
								new MessageSender(u.getSocket()).sendMessage(user.getName() + ": " + builder.toString(),
										Messages.CHAT_MESSAGE);
							}
						}
						System.out.println("-> " + user.getName() + ": " + builder.toString());

					} else {
						new MessageSender(socket).sendMessage("Du bist nicht eingeloggt!", Messages.ERROR_MESSAGE);
					}
					break;
				case Messages.LOGIN:
					String[] split = builder.toString().split("\\" + (char) ((byte) Messages.DELIMITER));
					try {
						if (split.length < 2) {
							new MessageSender(socket).sendMessage("LoginMessageFehler", Messages.LOGIN_ERROR_MESSAGE);
						} else {
							user = userManager.loginUser(split[0], split[1], socket);
							new MessageSender(socket).sendMessage("Login Erfolgreich", Messages.LOGIN_SUCCESS_MESSAGE);

						}

					} catch (UserAlreadyLoggedInException e1) {
						new MessageSender(socket).sendMessage("User ist bereits eingeloggt!",
								Messages.LOGIN_ERROR_MESSAGE);

					} catch (UserException e) {
						try {
							userManager.registerUser(split[0], split[1]);
							new MessageSender(socket).sendMessage("Registrierung erfolgreich!",
									Messages.SUCCESS_MESSAGE);

							try {
								user = userManager.loginUser(split[0], split[1], socket);
								new MessageSender(socket).sendMessage("Login Erfolgreich",
										Messages.LOGIN_SUCCESS_MESSAGE);
							} catch (UserException e1) {
								new MessageSender(socket).sendMessage("Fehler beim einloggen nach dem Registrieren!",
										Messages.ERROR_MESSAGE);
							}

						} catch (UserAlreadyExistsException e1) {
							new MessageSender(socket).sendMessage("Das eingegebene Passwort ist falsch!",
									Messages.LOGIN_ERROR_MESSAGE);
						}
					}
					break;
				}

			} catch (IOException e) {
				try {
					user.logout();
				} catch (IOException e1) {
				}
			}

		}
	}
}
