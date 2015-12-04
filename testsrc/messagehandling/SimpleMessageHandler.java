package messagehandling;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SimpleMessageHandler implements MessageHandler {

	private BlockingQueue<Message> messages;

	public SimpleMessageHandler() {
		this.messages = new LinkedBlockingQueue<>();
	}

	@Override
	public void handleMessage(Message message, Connection connection) {

		messages.add(message);

	}

	public Message getMessage() {
		try {
			return messages.take();
		} catch (InterruptedException e) {
		}
		return null;
	}

}