package messagehandling.messageinput;

import java.io.IOException;

import messagehandling.Message;
import messagehandling.MessageHandlerFactory;
import messagehandling.MessageType;
import server.ConnectionHandler;

public class ThreadedMessageListener extends Thread implements MessageListener {

	private ConnectionHandler connectionHandler;

	public ThreadedMessageListener(ConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Message message = connectionHandler.getConnection().getInputstream().readMessage();
				MessageHandlerFactory.getMessageHandler(message.getType()).handleMessage(message, connectionHandler);
			}
		} catch (IOException e) {
			MessageHandlerFactory.getMessageHandler(MessageType.DISCONNECT)
					.handleMessage(new Message(new byte[] {}, MessageType.DISCONNECT), connectionHandler);
		}
	}

}
