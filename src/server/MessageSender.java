package server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import chatshared.Messages;

public class MessageSender extends Thread {

	private Socket receiver;
	private int type;
	private byte[] message;

	public MessageSender(Socket receiver) {
		this.receiver = receiver;
	}

	public void sendMessage(byte[] message, int type) {
		if (message.length != 0) {
			this.message = message;
		}
		this.type = type;
		start();
	}

	public void sendMessage(String message, int type) {
		sendMessage(message.getBytes(), type);
	}

	@Override
	public void run() {
		try {
			OutputStream out = new BufferedOutputStream(receiver.getOutputStream());
			out.write(type);
			if (message != null) {
				out.write(message);
			}
			out.write(Messages.END_OF_MESSAGE);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
