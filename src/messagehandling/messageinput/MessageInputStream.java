package messagehandling.messageinput;

import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import messagehandling.Message;
import messagehandling.MessageType;

public class MessageInputStream extends FilterInputStream {

	public MessageInputStream(InputStream in) {
		super(in);
	}

	public Message readMessage() throws IOException {
		byte[] arr;
		DataInputStream input = new DataInputStream(in);
		int messageType = input.read();
		if (messageType == -1)
			throw new IOException("Stream closed");
		arr = new byte[input.readInt()];
		input.readFully(arr);
		return new Message(arr, MessageType.getType(messageType));
	}

}
