package server;

import java.net.Socket;

import messagehandling.Message;
import messagehandling.MessageSender;

public class Connection {

	private Socket socket;
	private MessageSender sender;

	public Connection(Socket socket) {
		this.socket = socket;
		this.sender = new MessageSender(this);
		sender.start();
	}

	public Socket getSocket() {
		return socket;
	}

	public void sendMessage(Message message) {
		sender.send(message);
	}
}
