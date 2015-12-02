package messagehandling;

import java.io.UnsupportedEncodingException;

public class Message {
	private byte[] message;
	private MessageType type;

	public Message(byte[] message, MessageType type) {
		this.message = message;
		this.type = type;
	}

	public Message(String message, MessageType type) {

		try {

			this.message = message.getBytes("UTF-8");
			this.type = type;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public MessageType getType() {
		return type;
	}

	public byte[] getMessage() {
		return message;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Message) {
			Message msg = (Message) obj;
			if (msg.getType() == getType() && msg.getMessage().equals(getMessage())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {

		try {
			return new String(getMessage(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(getMessage());
	}
}