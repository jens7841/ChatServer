package de.hff.ChatServer.messagehandling.messageinput;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hff.ChatServer.messagehandling.Message;
import de.hff.ChatServer.messagehandling.MessageType;

public class MessageInputStream extends FilterInputStream {

	DataInputStream input;;

	public MessageInputStream(InputStream in) {
		super(in);
		input = new DataInputStream(new BufferedInputStream(in, 65536));
	}

	public Message readMessage() throws IOException {
		byte[] arr;
		int messageType = input.read();
		if (messageType == -1)
			throw new IOException("Stream closed");
		arr = new byte[input.readInt()];
		input.readFully(arr);
		return new Message(arr, MessageType.getType(messageType));
	}

}
