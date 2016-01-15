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
		new Server().start();

	}

	public void initializeCommands() {
		// hier commands mit CommandHandler.addCommand hinzufügen
	}

	private UserManager userManager;
	private int port;
	private FileManager fileManager;

	public static final Properties PROPERTIES = new Properties();

	public Server() {
		loadProperties();
		userManager = new UserManager(PROPERTIES.getProperty("user.path", "users.csv"));
		try {
			port = Integer.parseInt(PROPERTIES.getProperty("port"));
		} catch (NumberFormatException e) {
			port = 12345;
		}
		this.fileManager = new FileManager(PROPERTIES.getProperty("files.path", "tmpFiles"));
	}

	public void start() {

		registerServices();

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