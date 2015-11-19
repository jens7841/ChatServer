package server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import usermanager.User;
import usermanager.UserAlreadyExistsException;
import usermanager.UserException;
import usermanager.Usermanager;

public class MessageListener extends Thread {
	public static final int DELIMITER = 0;
	public static final int END_OF_MESSAGE = 1;
	public static final int CHAT_MESSAGE = 2;
	public static final int LOGIN = 3;
	public static final int ERROR_MESSAGE = 4;
	public static final int SUCCESS_MESSAGE = 5;

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
				while ((read = in.read()) != END_OF_MESSAGE) {
					builder.append((char) read);
				}

				switch (first) {
				case CHAT_MESSAGE:

					break;
				case LOGIN:
					String[] split = builder.toString().split("\\" + (char) ((byte) DELIMITER));
					try {
						usermanager.userLogin(split[0], split[1], socket);
						new MessageSender(socket).sendMessage("Login erfolgreich!".getBytes(),
								MessageListener.SUCCESS_MESSAGE);
					} catch (UserException e) {
						try {
							usermanager.userRegistration(split[0], split[1]);
							new MessageSender(socket).sendMessage("Registrierung erfolgreich!".getBytes(),
									MessageListener.SUCCESS_MESSAGE);
						} catch (UserAlreadyExistsException e1) {
							new MessageSender(socket).sendMessage("Das eingegebene Passwort ist falsch!".getBytes(),
									MessageListener.ERROR_MESSAGE);
						}
						try {
							usermanager.userLogin(split[0], split[1], socket);
						} catch (UserException e1) {
							new MessageSender(socket).sendMessage("Fehler!".getBytes(), MessageListener.ERROR_MESSAGE);
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
