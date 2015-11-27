package server;

import java.io.IOException;
import java.net.ServerSocket;

import commands.Logout;
import filemanagement.FileManager;
import messagehandling.CommandHandler;
import messagehandling.MessageListener;
import usermanagement.UserManager;

public class Server extends Thread {

	private UserManager userManager;
	private FileManager fileManager;
	private ServerSocket serverSocket;
	private CommandHandler commandHandler;

	private int port;
	private boolean running;

	public Server(int port, String usersFileName, String filesTempPath) {
		this.port = port;
		this.userManager = new UserManager(usersFileName);
		this.fileManager = new FileManager(filesTempPath);
		this.commandHandler = new CommandHandler();
		initializeCommands();
	}

	private void initializeCommands() {
		commandHandler.addCommand(new Logout("logout", "exit", "quit"));
	}

	public void startServer() throws IOException {
		System.out.println("Starting Server....");
		serverSocket = new ServerSocket(port);
		this.running = true;
		this.start();
	}

	@Override
	public void run() {

		if (!running)
			return;

		try {
			connectionListener();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void connectionListener() throws IOException {
		while (running) {
			new MessageListener(new Connection(serverSocket.accept()), userManager, commandHandler).start();
		}
	}

}
