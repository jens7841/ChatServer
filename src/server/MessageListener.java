package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import chatshared.Messages;
import usermanager.User;
import usermanager.UserAlreadyExistsException;
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
				int first = in.read();

				StringBuilder builder = new StringBuilder();
				int read;
				while ((read = in.read()) != Messages.END_OF_MESSAGE) {
					builder.append((char) read);
				}

				switch (first) {
				case Messages.CHAT_MESSAGE:
					if (user != null) {
						for (User u : userManager.getUserList()) {

							if (u.isOnline()) {
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
						user = userManager.loginUser(split[0], split[1], socket);
						new MessageSender(socket).sendMessage("Login erfolgreich!".getBytes(),
								Messages.SUCCESS_MESSAGE);

						System.out.println("User " + split[0] + " hat sich eingeloggt!");

					} catch (UserException e) {
						try {
							userManager.registerUser(split[0], split[1]);
							new MessageSender(socket).sendMessage("Registrierung erfolgreich!".getBytes(),
									Messages.SUCCESS_MESSAGE);

							System.out.println("User: " + split[0] + " hat sich soeben registriert!");

						} catch (UserAlreadyExistsException e1) {
							new MessageSender(socket).sendMessage("Das eingegebene Passwort ist falsch!".getBytes(),
									Messages.ERROR_MESSAGE);
						}
						try {
							user = userManager.loginUser(split[0], split[1], socket);
						} catch (UserException e1) {
							new MessageSender(socket).sendMessage("Fehler!".getBytes(), Messages.ERROR_MESSAGE);
						}
					}
					break;
				}

			} catch (IOException e) {
				try {
					socket.close();
				} catch (IOException e1) {
				}
			}

		}
	}
}
