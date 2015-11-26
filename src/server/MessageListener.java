package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
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
	private FileManager fileManager;
	private CommandHandler commandHandler;

	public MessageListener(Socket socket, UserManager usermanager, FileManager fileManager,
			CommandHandler commandHandler) {
		this.fileManager = fileManager;
		this.socket = socket;
		this.userManager = usermanager;
		this.commandHandler = commandHandler;
	}

	@Override
	public void run() {

		while (!socket.isClosed()) {

			try {
				InputStream in = new BufferedInputStream(socket.getInputStream());
				int messageType = in.read();
				StringBuilder builder = new StringBuilder();

				if (messageType == Messages.FILE) {

					StringBuilder fileName = new StringBuilder();

					{
						int read;

						while ((read = in.read()) != Messages.DELIMITER) {
							fileName.append((char) read);
						}
					}

					StringBuilder byteLengthBuilder = new StringBuilder();

					{
						int read;
						while ((read = in.read()) != Messages.DELIMITER) {
							byteLengthBuilder.append((char) read);
						}
					}

					long byteLength = Long.valueOf(byteLengthBuilder.toString());
					File writtenFile = new File(fileManager.getTempPath());
					if (!writtenFile.exists()) {
						writtenFile.mkdir();
					}
					FileOutputStream writer = new FileOutputStream(
							writtenFile.getAbsolutePath() + "/" + fileName.toString());

					for (int i = 0; i < byteLength; i++) {
						writer.write(in.read());
					}
					in.read();
					writer.close();

				} else {

					int read;
					while ((read = in.read()) != Messages.END_OF_MESSAGE) {
						if (read == -1) {
							throw new IOException();
						}
						builder.append((char) read);
					}
				}

				switch (messageType) {

				case Messages.CHAT_MESSAGE:
					if (user != null) {

						if (builder.charAt(0) == '/') {
							commandHandler.handleCommand(user, builder.toString().substring(1));
							continue;
						}

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
						} catch (UserException e2) {
							new MessageSender(socket).sendMessage(e2.getMessage(), Messages.LOGIN_ERROR_MESSAGE);
						}
					}
					break;
				}

			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}

		try {
			user.logout();
		} catch (IOException e1) {
		}

	}
}