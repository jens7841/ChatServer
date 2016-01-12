package server;

import messagehandling.messagehandler.ChatMessageHandler;
import messagehandling.messagehandler.LoginMessageHandler;
import messagehandling.messagehandler.UploadPackageMessageHandler;
import messagehandling.messagehandler.UploadRequestMessageHandler;
import usermanagement.UserManager;

public class Server extends Thread {

	private String filename;

	public Server(String filename) {
		this.filename = filename;
	}

	@Override
	public void run() {
	}

	@Override
	public synchronized void start() {
		ServiceRegistry.fillHashMap(new ChatMessageHandler(), ServiceRegistry.chatMessageHandler);
		ServiceRegistry.fillHashMap(new LoginMessageHandler(null), ServiceRegistry.loginMessageHandler); // TODO
		ServiceRegistry.fillHashMap(new UploadPackageMessageHandler(), ServiceRegistry.uploadPackageMessageHandler);
		ServiceRegistry.fillHashMap(new UploadRequestMessageHandler(), ServiceRegistry.uploadRequestMessageHandler);

		UserManager usermanager = new UserManager(filename);

	}
}