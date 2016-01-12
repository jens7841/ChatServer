package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import messagehandling.MessageInputStream;
import messagehandling.MessageOutputstream;
import messagehandling.ThreadedMessageListener;

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

				Connection connection = new Connection(new MessageOutputstream(socket.getOutputStream()),
						new MessageInputStream(socket.getInputStream()));

				ThreadedMessageListener listener = new ThreadedMessageListener(connection);

				listener.start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}