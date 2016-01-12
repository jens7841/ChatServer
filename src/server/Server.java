package server;

import messagehandling.messagehandler.ChatMessageHandler;
import messagehandling.messagehandler.LoginMessageHandler;
import messagehandling.messagehandler.UploadPackageMessageHandler;
import messagehandling.messagehandler.UploadRequestMessageHandler;
import usermanagement.UserManager;

public class Server {

	public static void main(String[] args) {
		new Server(12345, "users.csv").start();

	}

	private String filename;
	private int port;

	public Server(int port, String filename) {
		this.filename = filename;
		this.port = port;
	}

	public void start() {
		UserManager usermanager = new UserManager(filename);

		ServiceRegistry.fillHashMap(new ChatMessageHandler(), ServiceRegistry.chatMessageHandler);
		ServiceRegistry.fillHashMap(new LoginMessageHandler(usermanager), ServiceRegistry.loginMessageHandler);
		ServiceRegistry.fillHashMap(new UploadPackageMessageHandler(), ServiceRegistry.uploadPackageMessageHandler);
		ServiceRegistry.fillHashMap(new UploadRequestMessageHandler(), ServiceRegistry.uploadRequestMessageHandler);

		ConnectionListener connectionListener = new ConnectionListener(port);
		connectionListener.start();

	}
}