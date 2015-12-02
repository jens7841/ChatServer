package server;

import java.net.Socket;

import messagehandling.Message;
import messagehandling.MessageSender;

public class Connection {

	private Socket socket;
	private MessageSender sender;

	public Connection(Socket socket, MessageSender sender) {
		this.socket = socket;
		this.sender = sender;
	}

	public Socket getSocket() {
		return socket;
	}

	public void sendMessage(Message message) {
		sender.sendMessage(message);
	}
}