package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import chatshared.Messages;
import usermanager.User;
import usermanager.UserAlreadyExistsException;
import usermanager.UserException;
import usermanager.Usermanager;

public class MessageListener extends Thread {

	private Socket socket;
	private User user;
	private Usermanager usermanager;

	public MessageListener(Socket socket, Usermanager usermanager) {
		this.socket = socket;
		this.usermanager = usermanager;
		this.start();
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
					for (User u : usermanager.getUserList()) {
						if (user != null) {
							new MessageSender(socket).sendMessage(builder.toString().getBytes(), Messages.CHAT_MESSAGE);
						} else {
							new MessageSender(socket).sendMessage(builder.toString().getBytes(), Messages.CHAT_MESSAGE);
						}
					}
					break;
				case Messages.LOGIN:
					String[] split = builder.toString().split("\\" + (char) ((byte) Messages.DELIMITER));
					try {
						user = usermanager.userLogin(split[0], split[1], socket);
						new MessageSender(socket).sendMessage("Login erfolgreich!".getBytes(),
								Messages.SUCCESS_MESSAGE);
					} catch (UserException e) {
						try {
							usermanager.userRegistration(split[0], split[1]);
							new MessageSender(socket).sendMessage("Registrierung erfolgreich!".getBytes(),
									Messages.SUCCESS_MESSAGE);
						} catch (UserAlreadyExistsException e1) {
							new MessageSender(socket).sendMessage("Das eingegebene Passwort ist falsch!".getBytes(),
									Messages.ERROR_MESSAGE);
						}
						try {
							user = usermanager.userLogin(split[0], split[1], socket);
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
