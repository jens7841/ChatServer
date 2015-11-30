package messagehandling;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import filemanagement.FileManager;
import server.Connection;
import usermanagement.UserManager;

public class MessageListener extends Thread {

	private MessageHandler messageHandler;
	private Connection connection;

	public MessageListener(Connection connection, UserManager userManager, CommandHandler commandHandler,
			FileManager fileManager) {
		this.connection = connection;
		this.messageHandler = new MessageHandler(connection, userManager, commandHandler, fileManager);
	}

	@Override
	public void run() {

		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(connection.getSocket().getInputStream()));

			while (!connection.getSocket().isClosed()) {
				int firstByte;
				if ((firstByte = in.read()) == -1) {
					throw new IOException();
				}

				MessageType messageType = MessageType.getType(firstByte);

				int inputLenght = in.readInt();

				byte[] input = new byte[inputLenght];

				in.readFully(input);

				Message message = new Message(input, messageType);

				switch (messageType) {
				case CHAT_MESSAGE:
					messageHandler.chatMessage(message);
					break;
				case LOGIN:
					messageHandler.loginMessage(message);
					break;
				case UPLOAD_REQUEST:
					messageHandler.uploadRequest(message);
					break;
				default:
					break;
				}
				System.out.println(connection.getSocket().getLocalPort());
			}

		} catch (IOException e) {
			try {
				connection.getSocket().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

}
