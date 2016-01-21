package chatserver.messagehandling.messageinput;

import java.io.IOException;

import chatserver.messagehandling.Message;
import chatserver.messagehandling.MessageHandlerFactory;
import chatserver.messagehandling.MessageType;
import chatserver.server.UserHandler;

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
