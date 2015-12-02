package messagehandling;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import filemanagement.FileManager;
import filemanagement.FileSaver;
import filemanagement.UploadedFile;
import server.Connection;
import usermanagement.User;
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

	public void logout() {
		if (user != null) {
			try {
				user.logout();
			} catch (IOException e) {
			}
		}
	}

	public void uploadRequest(Message message) {

		try {

			DataInputStream in = new DataInputStream(
					new BufferedInputStream(new ByteArrayInputStream(message.getMessage())));

			int fileNameLength = in.readInt();
			byte[] fileNameArray = new byte[fileNameLength];
			in.readFully(fileNameArray);
			String fileName = new String(fileNameArray, "UTF-8");

			long size = in.readLong();

			if (user == null) {
				connection.sendMessage(new Message(message.getMessage(), MessageType.UPLOAD_REJECT));
				return;
			}

			UploadedFile file = fileManager.saveFile(fileName, size, user);

			byte[] confirmation = ByteBuffer.allocate(message.getMessage().length + 4).put(message.getMessage())
					.putInt(file.getId()).array();

			connection.sendMessage(new Message(confirmation, MessageType.UPLOAD_CONFIRMATION));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void uploadBegin(Message message) {
		try {

			DataInputStream in = new DataInputStream(
					new BufferedInputStream(new ByteArrayInputStream(message.getMessage())));

			int fileID = in.readInt();
			long size = in.readLong();

			int fileNameLength = in.readInt();
			byte[] fileNameArray = new byte[fileNameLength];
			in.readFully(fileNameArray);
			String fileName = new String(fileNameArray, "UTF-8");

			int dataLength = in.readInt();

			byte[] data = new byte[dataLength];
			in.readFully(data);

			UploadedFile file = fileManager.getFile(fileID);
			if (file == null) {
				connection.sendMessage(new Message("Error while uploading a File!", MessageType.ERROR_MESSAGE));
			} else {
				fileManager.getSaver(file).addPackgage(data);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int i = 0;

	public void uploadPackage(Message message) {
		try {
			System.out.println("count: " + (++i));

			DataInputStream in = new DataInputStream(
					new BufferedInputStream(new ByteArrayInputStream(message.getMessage())));

			int fileID = in.readInt();
			int dataLength = in.readInt();
			System.out.println("Fileid" + fileID);
			System.out.println("data Length" + dataLength);

			byte[] data = new byte[dataLength];
			in.readFully(data);

			UploadedFile file = fileManager.getFile(fileID);

			FileSaver saver = fileManager.getSaver(file);
			if (saver != null) {
				System.out.println("save DATA");
				saver.addPackgage(data);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}