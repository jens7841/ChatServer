package messagehandling.messageinput;

import java.io.IOException;

import messagehandling.Message;
import messagehandling.MessageHandlerFactory;
import messagehandling.MessageType;
import server.UserHandler;

public class ThreadedMessageListener extends Thread implements MessageListener {

	private UserHandler connectionHandler;

	public ThreadedMessageListener(UserHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Message message = connectionHandler.getUser().getConnection().getInputstream().readMessage();
				MessageHandlerFactory.getMessageHandler(message.getType()).handleMessage(message, connectionHandler);

			}
		} catch (IOException e) {
			MessageHandlerFactory.getMessageHandler(MessageType.DISCONNECT)
					.handleMessage(new Message(new byte[] {}, MessageType.DISCONNECT), connectionHandler);
		}
	}

}
