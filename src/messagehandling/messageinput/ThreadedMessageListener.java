package messagehandling.messageinput;

import java.io.IOException;

import messagehandling.Message;
import messagehandling.MessageHandlerFactory;
import messagehandling.MessageType;
import server.UserHandler;

public class ThreadedMessageListener extends Thread implements MessageListener {

	private UserHandler userHandler;

	public ThreadedMessageListener(UserHandler userHandler) {
		this.userHandler = userHandler;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Message message = userHandler.getUser().getConnection().getInputstream().readMessage();
				MessageHandlerFactory.getMessageHandler(message.getType()).handleMessage(message, userHandler);

			}
		} catch (IOException e) {
			MessageHandlerFactory.getMessageHandler(MessageType.DISCONNECT)
					.handleMessage(new Message(new byte[] {}, MessageType.DISCONNECT), userHandler);
		}
	}

}
