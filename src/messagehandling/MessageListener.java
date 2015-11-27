package messagehandling;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import chatshared.Messages;
import server.Connection;
import usermanagement.UserManager;

public class MessageListener extends Thread {

	private MessageHandler messageHandler;
	private Connection connection;
	private UserManager userManager;

	public MessageListener(Connection connection, UserManager userManager) {
		this.connection = connection;
		this.userManager = userManager;
		this.messageHandler = new MessageHandler(connection, userManager);
	}

	@Override
	public void run() {

		try {
			InputStream in = new BufferedInputStream(connection.getSocket().getInputStream());

			while (!connection.getSocket().isClosed()) {
				int firstByte;
				if ((firstByte = in.read()) == -1) {
					throw new IOException();
				}

				MessageType messageType = MessageType.getType(firstByte);

				StringBuilder builder = new StringBuilder();

				int read;
				while ((read = in.read()) != Messages.END_OF_MESSAGE) {
					if (read == -1)
						throw new IOException();
					builder.append((char) read);
				}

				Message message = new Message(builder.toString(), messageType);

				switch (messageType) {
				case CHAT_MESSAGE:
					messageHandler.chatMessage(message);
					break;
				case LOGIN:
					System.out.println(message);
					messageHandler.loginMessage(message);
					break;
				default:
					break;
				}
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
