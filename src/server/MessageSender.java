package server;

import java.net.Socket;

import usermanager.User;

public class MessageSender implements Runnable {

	private Socket receiver;
	private Socket sender;

	public MessageSender(User sender, User receiver) {
		// do magic stuff
	}

	public MessageSender(Socket sender, Socket receiver) {
		this.sender = sender;
		this.receiver = receiver;
	}

	public void sendMessage(byte[] message, int type) {
		// TODO
	}

	@Override
	public void run() {

	}
}
