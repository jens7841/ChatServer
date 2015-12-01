package messagehandling;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;

import filemanagement.FileListener;
import filemanagement.FileManager;
import server.Connection;
import usermanagement.User;
<<<<<<< HEAD
import usermanagement.UserAlreadyExistsException;
=======
>>>>>>> branch 'master' of https://github.com/jens7841/ChatServer.git
import usermanagement.UserAlreadyLoggedInException;
import usermanagement.UserException;
import usermanagement.UserManager;

public class MessageHandler {

	private User user;
	private Connection connection;
	private UserManager userManager;
	private CommandHandler commandHandler;

	private FileManager fileManager;

	public MessageHandler(Connection connection, UserManager userManager, CommandHandler commandHandler,
			FileManager fileManager) {
		this.userManager = userManager;
		this.connection = connection;
		this.commandHandler = commandHandler;
		this.fileManager = fileManager;
	}

	public void loginMessage(Message message) {
		if (user == null) {
			try {

				DataInputStream in = new DataInputStream(
						new BufferedInputStream(new ByteArrayInputStream(message.getMessage())));

				byte[] usernameBytes = new byte[in.readInt()];
				in.readFully(usernameBytes);
				String username = new String(usernameBytes, "UTF-8");

				byte[] passwordBytes = new byte[in.readInt()];
				in.readFully(passwordBytes);
				String password = new String(passwordBytes, "UTF-8");

				if (!userManager.userExists(username)) {
					userManager.registerUser(username, password);
					connection.sendMessage(new Message("Erfolgreich Registriert!", MessageType.SUCCESS_MESSAGE));
<<<<<<< HEAD

					user = userManager.loginUser(username, password, connection);

					connection.sendMessage(new Message("Erfolgreich eingeloggt!", MessageType.LOGIN_SUCCESS_MESSAGE));

				} catch (UserAlreadyExistsException e) {

					try {
						user = userManager.loginUser(username, password, connection);
						connection
								.sendMessage(new Message("Erfolgreich eingeloggt!", MessageType.LOGIN_SUCCESS_MESSAGE));

					} catch (UserAlreadyLoggedInException e1) {
						connection.sendMessage(
								new Message("Der User ist bereits eingeloggt!", MessageType.LOGIN_ERROR_MESSAGE));
					} catch (UserException e1) {
						connection.sendMessage(new Message(e.getMessage(), MessageType.LOGIN_ERROR_MESSAGE));
					}

				} catch (UserException e) {
					connection.sendMessage(new Message(e.getMessage(), MessageType.LOGIN_ERROR_MESSAGE));
=======
>>>>>>> branch 'master' of https://github.com/jens7841/ChatServer.git
				}
				user = userManager.loginUser(username, password, connection);
				connection.sendMessage(new Message("Erfolgreich eingeloggt!", MessageType.LOGIN_SUCCESS_MESSAGE));

			} catch (UserAlreadyLoggedInException e) {
				connection.sendMessage(
						new Message("Dieser User ist bereits eingeloggt!", MessageType.LOGIN_ERROR_MESSAGE));
			} catch (UserException e) {
				connection.sendMessage(new Message(e.getMessage(), MessageType.LOGIN_ERROR_MESSAGE));
			} catch (IOException e) {
				connection.sendMessage(new Message("LoginMessageFehler!", MessageType.LOGIN_ERROR_MESSAGE));
			}

		} else {
			connection.sendMessage(new Message("LoginMessageFehler!", MessageType.LOGIN_ERROR_MESSAGE));
		}
	}

	public void chatMessage(Message message) {
		if (user == null) {
			connection.sendMessage(new Message("Du bist nicht eingeloggt!", MessageType.LOGIN_ERROR_MESSAGE));
		} else {

			if (message.toString().startsWith("/")) {
				if (!commandHandler.handleCommand(message, user)) {
					System.out.println("Command not found! " + user.getName() + " " + message.toString());
					connection.sendMessage(new Message("Befehl nicht gefunden!", MessageType.ERROR_MESSAGE));
				}
			} else {

				Message toAllMessage = new Message(user.getName() + ": " + message.toString(),
						MessageType.CHAT_MESSAGE);
				userManager.sendToAllUsers(toAllMessage, user);

				System.out.println("-> " + toAllMessage);
			}
		}
	}

	public void uploadRequest(Message message) {
		ServerSocket fileSocket = null;
		int port = 0;
		while (fileSocket == null) {
			try {
				port = (int) Math.random() * 10000 + 300;
				fileSocket = new ServerSocket(port);
			} catch (IOException e) {
			}
		}
		FileListener fileListener = new FileListener(fileSocket, fileManager);
		fileListener.start();
		connection.sendMessage(new Message(String.valueOf(port), MessageType.UPLOAD_CONFIRMATION));
	}

	public void logout() {
		if (user != null) {
			try {
				user.logout();
			} catch (IOException e) {
			}
		}
	}

}
