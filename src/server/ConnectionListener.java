package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import messagehandling.messageinput.MessageInputStream;
import messagehandling.messageoutput.MessageOutputstream;

public class ConnectionListener {

	private ServerSocket serverSocket;

	public ConnectionListener(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		while (!serverSocket.isClosed()) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("Client connected to Server :o IP: " + socket.getInetAddress());

				Connection connection = new Connection(new MessageOutputstream(socket.getOutputStream()),
						new MessageInputStream(socket.getInputStream()));

				new ConnectionHandler(connection);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}