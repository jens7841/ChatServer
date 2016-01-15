package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import commandhandling.CommandHandler;
import filehandling.FileManager;
import messagehandling.messagehandler.ChatMessageHandler;
import messagehandling.messagehandler.CommandMessageHandler;
import messagehandling.messagehandler.DisconnectMessageHandler;
import messagehandling.messagehandler.LoginMessageHandler;
import messagehandling.messagehandler.UploadPackageMessageHandler;
import messagehandling.messagehandler.UploadRequestMessageHandler;
import usermanagement.UserManager;

public class Server {

	public static void main(String[] args) {
		new Server(12345, "users.csv", "tmpFiles").start();

	}

	public void initializeCommands() {
		// hier commands mit CommandHandler.addCommand hinzufügen
	}

	private UserManager userManager;
	private int port;
	private FileManager fileManager;

	public static final Properties PROPERTIES = new Properties();

	public Server(int port, String filename, String pathName) {
		userManager = new UserManager(filename);
		this.port = port;
		this.fileManager = new FileManager(pathName);
	}

	public void start() {

		registerServices();
		loadProperties();

		ConnectionListener connectionListener = new ConnectionListener(port);
		connectionListener.start();

	}

	private void loadProperties() {

		FileInputStream propFileStream = null;
		try {
			propFileStream = new FileInputStream(new File("server.properties"));

			PROPERTIES.load(propFileStream);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (propFileStream != null) {
				try {
					propFileStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void registerServices() {
		ServiceRegistry.register(new ChatMessageHandler(userManager), ServiceRegistry.chatMessageHandler);
		ServiceRegistry.register(new LoginMessageHandler(userManager), ServiceRegistry.loginMessageHandler);
		ServiceRegistry.register(new DisconnectMessageHandler(userManager), ServiceRegistry.disconnectMessageHandler);
		ServiceRegistry.register(new UploadPackageMessageHandler(fileManager),
				ServiceRegistry.uploadPackageMessageHandler);
		ServiceRegistry.register(new UploadRequestMessageHandler(fileManager),
				ServiceRegistry.uploadRequestMessageHandler);
		ServiceRegistry.register(new CommandMessageHandler(new CommandHandler()),
				ServiceRegistry.commandMessageHandler);
	}
}