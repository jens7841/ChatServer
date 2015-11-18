package server;

import java.io.IOException;
import java.net.ServerSocket;

import usermanager.Usermanager;

public class Server {

	private Usermanager usermanager;
	// private ArrayList<Räume> räume
	private ServerSocket serverSocket;
	private boolean running;
	private int port;

	public Server(int port, String usersFileName) {
		this.port = port;
		this.running = true;
		usermanager = new Usermanager(usersFileName);
	}

	public void start() throws IOException {
		serverSocket = new ServerSocket(port);
		connectionListener();
	}

	private void connectionListener() throws IOException {
		while (running) {
			serverSocket.accept();
		}
	}

}
