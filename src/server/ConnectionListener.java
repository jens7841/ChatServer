package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import messagehandling.messageinput.MessageInputStream;
import messagehandling.messageoutput.MessageOutputstream;

public class ConnectionListener {

	private int port;

	public ConnectionListener(int port) {
		this.port = port;
	}

	public void start() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);

			while (!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("Client connected to Server :o IP: " + socket.getInetAddress());

				Connection connection = new Connection(new MessageOutputstream(socket.getOutputStream()),
						new MessageInputStream(socket.getInputStream()));

				new UserHandler(connection);

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
		}
	}
}