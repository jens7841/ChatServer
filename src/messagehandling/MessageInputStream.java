package messagehandling;

import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MessageInputStream extends FilterInputStream {

	protected MessageInputStream(InputStream in) {
		super(in);
	}

	public Message readMessage() throws IOException {
		byte[] arr;
		DataInputStream input = new DataInputStream(in);
		int messageType = input.read();
		arr = new byte[input.readInt()];
		input.readFully(arr);
		return new Message(arr, MessageType.getType(messageType));
	}

}
