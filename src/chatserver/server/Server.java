package chatserver.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import chatserver.commandhandling.CommandHandler;
import chatserver.filehandling.FileManager;
import chatserver.messagehandling.messagehandler.ChatMessageHandler;
import chatserver.messagehandling.messagehandler.CommandMessageHandler;
import chatserver.messagehandling.messagehandler.DisconnectMessageHandler;
import chatserver.messagehandling.messagehandler.LoginMessageHandler;
import chatserver.messagehandling.messagehandler.UploadPackageMessageHandler;
import chatserver.messagehandling.messagehandler.UploadRequestMessageHandler;
import chatserver.usermanagement.UserManager;

public class Server {

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