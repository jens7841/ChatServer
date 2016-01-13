package server;

import commandhandling.CommandHandler;
import messagehandling.messagehandler.ChatMessageHandler;
import messagehandling.messagehandler.CommandMessageHandler;
import messagehandling.messagehandler.DisconnectMessageHandler;
import messagehandling.messagehandler.LoginMessageHandler;
import messagehandling.messagehandler.UploadPackageMessageHandler;
import messagehandling.messagehandler.UploadRequestMessageHandler;
import usermanagement.UserManager;

public class Server {

	public static void main(String[] args) {
		new Server(12345, "users.csv").start();

	}

	public void initializeCommands() {
		// hier commands mit CommandHandler.addCommand hinzufügen
	}

	private String filename;
	private int port;

	public Server(int port, String filename) {
		this.filename = filename;
		this.port = port;
	}

	public void start() {
		UserManager userManager = new UserManager(filename);

		ServiceRegistry.register(new ChatMessageHandler(userManager), ServiceRegistry.chatMessageHandler);
		ServiceRegistry.register(new LoginMessageHandler(userManager), ServiceRegistry.loginMessageHandler);
		ServiceRegistry.register(new DisconnectMessageHandler(userManager), ServiceRegistry.disconnectMessageHandler);
		ServiceRegistry.register(new UploadPackageMessageHandler(), ServiceRegistry.uploadPackageMessageHandler);
		ServiceRegistry.register(new UploadRequestMessageHandler(), ServiceRegistry.uploadRequestMessageHandler);
		ServiceRegistry.register(new CommandMessageHandler(new CommandHandler()),
				ServiceRegistry.commandMessageHandler);

		ConnectionListener connectionListener = new ConnectionListener(port);
		connectionListener.start();

	}
}