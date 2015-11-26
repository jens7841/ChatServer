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

					File folder = new File(fileManager.getTempPath());

					if (!folder.exists()) {
						folder.mkdir();
					}

					FileOutputStream writer = new FileOutputStream(
							fileManager.getTempPath() + "/" + fileName.toString());

					for (int i = 0; i < byteLength; i++) {
						writer.write(in.read());
					}
					in.read();
					writer.close();
					File file = new File(fileManager.getTempPath() + "/" + fileName.toString());
					UploadedFile uploadedFile = new UploadedFile(file, fileManager.getLastID(), user);
					fileManager.addFile(uploadedFile);

					userManager.sendToAllUsers("Der User " + user.getName() + " hat soeben die Datei "
							+ uploadedFile.getFile().getName() + " hochgeladen. ID: " + uploadedFile.getId(),
							Messages.CHAT_MESSAGE);
					System.out.println("User: " + user.getName() + " uploaded " + uploadedFile.getFile().getName()
							+ ", ID: " + uploadedFile.getId());

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
								new MessageSender(u).sendMessage(user.getName() + ": " + builder.toString(),
										Messages.CHAT_MESSAGE);
							}
						}
						System.out.println("-> " + user.getName() + ": " + builder.toString());

					} else {
						new MessageSender(user).sendMessage("Du bist nicht eingeloggt!", Messages.ERROR_MESSAGE);
					}
					break;

				case Messages.LOGIN:
					String[] split = builder.toString().split("\\" + (char) ((byte) Messages.DELIMITER));
					try {
						if (split.length < 2) {
							new MessageSender(user).sendMessage("LoginMessageFehler", Messages.LOGIN_ERROR_MESSAGE);
						} else {
							user = userManager.loginUser(split[0], split[1], socket);
							new MessageSender(user).sendMessage("Login Erfolgreich", Messages.LOGIN_SUCCESS_MESSAGE);

						}

					} catch (UserAlreadyLoggedInException e1) {
						new MessageSender(user).sendMessage("User ist bereits eingeloggt!",
								Messages.LOGIN_ERROR_MESSAGE);

					} catch (UserException e) {
						try {
							userManager.registerUser(split[0], split[1]);
							new MessageSender(user).sendMessage("Registrierung erfolgreich!", Messages.SUCCESS_MESSAGE);

							try {
								user = userManager.loginUser(split[0], split[1], socket);
								new MessageSender(user).sendMessage("Login Erfolgreich",
										Messages.LOGIN_SUCCESS_MESSAGE);
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
					break;
				}

			} catch (IOException e) {
				break;
			}
		}

		try {
			user.logout();
		} catch (IOException e1) {
		}

	}
}