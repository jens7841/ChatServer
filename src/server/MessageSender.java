package server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import chatshared.Messages;
import usermanager.User;

public class MessageSender extends Thread {

	private User receiver;
	private int messageType;
	private byte[] message;

	public MessageSender(User receiver) {
		this.receiver = receiver;
	}

	public void sendMessage(byte[] message, int messageType) {
		if (message.length != 0) {
			this.message = message;
		}
		this.messageType = messageType;
		start();
	}

	public void sendMessage(String message, int messageType) {
		sendMessage(message.getBytes(), messageType);
	}

	@Override
	public void run() {
		try {
			OutputStream out = new BufferedOutputStream(receiver.getOutputStream());
			out.write(messageType);
			if (message != null) {
				out.write(message);
			}
			out.write(Messages.END_OF_MESSAGE);
			out.flush();
		} catch (IOException e) {
		}

	}

}
