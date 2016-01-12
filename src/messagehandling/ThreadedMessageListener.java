package messagehandling;

import java.io.IOException;
import java.io.InputStream;

public class ThreadedMessageListener extends MessageListener {

	public ThreadedMessageListener(InputStream in) {
		super(in);
	}

	@Override
	public void run() {
		byte[] arr;

		try {
			int messageType = in.read();
			arr = new byte[in.readInt()];
			in.readFully(arr);
			Message message = new Message(arr, MessageType.getType(messageType));
			MessageHandlerFactory.getMessageHandler(MessageType.getType(messageType)).handleMessage(message);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
