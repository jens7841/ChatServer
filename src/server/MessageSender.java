package server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import chatshared.Messages;
import usermanager.User;

public class MessageSender extends Thread {

	private Socket receiver;
	private int type;
	private byte[] message;

	public MessageSender(User receiver) {
		this.receiver = receiver.getSocket();
	}

	public MessageSender(Socket receiver) {
		this.receiver = receiver;
	}

	public void sendMessage(byte[] message, int type) {
		this.message = message;
		this.type = type;
		start();
	}

	@Override
	public void run() {
		try {
			OutputStream out = new BufferedOutputStream(receiver.getOutputStream());
			out.write(type);
			out.write(message);
			out.write(Messages.END_OF_MESSAGE);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
