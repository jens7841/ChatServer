package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import messagehandling.Connection;
import messagehandling.ThreadedMessageListener;
import messagehandling.MessageSender;

public class ConnectionListener extends Thread {

	private ServerSocket serverSocket;

	public ConnectionListener(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!serverSocket.isClosed()) {
			try {
				Socket socket = serverSocket.accept();

				ThreadedMessageListener listener = new ThreadedMessageListener(socket.getInputStream());
				listener.start();

				new Connection(listener, new MessageSender(socket.getOutputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
