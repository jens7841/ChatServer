package messagehandling.messageinput;

import java.io.IOException;

import messagehandling.Message;
import messagehandling.MessageHandlerFactory;
import server.Connection;

public class ThreadedMessageListener extends Thread implements MessageListener {

	private Connection connection;

	public ThreadedMessageListener(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		try {

			Message message = connection.getInputstream().readMessage();
			MessageHandlerFactory.getMessageHandler(message.getType()).handleMessage(message, connection);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
