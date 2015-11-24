package server;

import java.io.IOException;
import java.net.ServerSocket;

import commands.Logout;
import usermanager.UserManager;

public class Server extends Thread {

	private UserManager userManager;
	private FileManager fileManager;
	private CommandHandler commandHandler;
	private ServerSocket serverSocket;
	private boolean running;
	private int port;

	public Server(int port, String usersFileName) {
		this.port = port;
		userManager = new UserManager(usersFileName);
		commandHandler = new CommandHandler();
		fileManager = new FileManager(userManager);
		initializeCommands();
	}

	public void startServer() throws IOException {
		serverSocket = new ServerSocket(port);
		this.running = true;
		this.start();
	}

	private void connectionListener() throws IOException {
		while (running) {
			new MessageListener(serverSocket.accept(), userManager, fileManager, commandHandler).start();
		}
	}

	private void initializeCommands() {
		commandHandler.addCommand(new Logout("logout", "quit", "exit"));

	}

	@Override
	public void run() {
		try {
			connectionListener();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
