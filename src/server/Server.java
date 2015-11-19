package server;

import java.io.IOException;
import java.net.ServerSocket;

import usermanager.UserManager;

public class Server {

	private UserManager userManager;
	private ServerSocket serverSocket;
	private boolean running;
	private int port;

	public Server(int port, String usersFileName) {
		this.port = port;
		this.running = true;
		userManager = new UserManager(usersFileName);
	}

	public void startServer() throws IOException {
		serverSocket = new ServerSocket(port);
		connectionListener();
	}

	private void connectionListener() throws IOException {
		while (running) {
			new MessageListener(serverSocket.accept(), userManager).start();
		}
	}

}
