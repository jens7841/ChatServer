package de.hff.ChatServer.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import de.hff.ChatServer.commandhandling.CommandHandler;
import de.hff.ChatServer.filehandling.FileManager;
import de.hff.ChatServer.messagehandling.messagehandler.ChatMessageHandler;
import de.hff.ChatServer.messagehandling.messagehandler.CommandMessageHandler;
import de.hff.ChatServer.messagehandling.messagehandler.DisconnectMessageHandler;
import de.hff.ChatServer.messagehandling.messagehandler.LoginMessageHandler;
import de.hff.ChatServer.messagehandling.messagehandler.UploadPackageMessageHandler;
import de.hff.ChatServer.messagehandling.messagehandler.UploadRequestMessageHandler;
import de.hff.ChatServer.usermanagement.UserManager;

public class Server {

	public void initializeCommands() {
		// hier commands mit CommandHandler.addCommand hinzuf�gen
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
			File file = new File("server.properties");
			System.out.println(file.getAbsolutePath());
			propFileStream = new FileInputStream(file);

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