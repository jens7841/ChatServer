package server;

import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import messagehandling.Message;
import messagehandling.MessageSender;

public class Connection {

	private Socket socket;
	private BlockingQueue<Message> buffer;

	public Connection(Socket socket) {
		this.socket = socket;
		this.buffer = new LinkedBlockingQueue<>();
		new MessageSender(this).start();
	}

	public Socket getSocket() {
		return socket;
	}

	public BlockingQueue<Message> getBuffer() {
		return buffer;
	}

	public void sendMessage(Message message) {
		try {
			buffer.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
