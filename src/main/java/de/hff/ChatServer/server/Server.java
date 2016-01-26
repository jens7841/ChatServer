package de.hff.ChatServer.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.hff.ChatServer.commandhandling.CommandHandler;
import de.hff.ChatServer.filehandling.FileManager;
import de.hff.ChatServer.messagehandling.messagehandler.ChatMessageHandler;
import de.hff.ChatServer.messagehandling.messagehandler.CommandMessageHandler;
import de.hff.ChatServer.messagehandling.messagehandler.DisconnectMessageHandler;
import de.hff.ChatServer.messagehandling.messagehandler.DownloadRequestMessageHandler;
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

		FileInputStream in = null;
		File file = new File("server.properties");

		if (file.exists()) {

			try {
				in = new FileInputStream(file);

				PROPERTIES.load(in);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} else {
			FileOutputStream out = null;
			try {
				InputStream input = getClass().getResourceAsStream("/server.properties");
				out = new FileOutputStream(file);
				for (int read = 0; (read = input.read()) != -1;) {
					out.write(read);
				}
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void registerServices() {
		ServiceRegistry.register(new ChatMessageHandler(userManager), ServiceRegistry.CHAT_MESSAGE_HANDLER);
		ServiceRegistry.register(new LoginMessageHandler(userManager), ServiceRegistry.LOGIN_MESSAGE_HANDLER);
		ServiceRegistry.register(new DisconnectMessageHandler(userManager), ServiceRegistry.DISCONNECT_MESSAGE_HANDLER);
		ServiceRegistry.register(new UploadPackageMessageHandler(fileManager),
				ServiceRegistry.UPLOAD_PACKAGE_MESSAGE_HANDLER);
		ServiceRegistry.register(new UploadRequestMessageHandler(fileManager),
				ServiceRegistry.UPLOAD_REQUEST_MESSAGE_HANDLER);
		ServiceRegistry.register(new CommandMessageHandler(new CommandHandler()),
				ServiceRegistry.COMMAND_MESSAGE_HANDLER);
		ServiceRegistry.register(new DownloadRequestMessageHandler(fileManager),
				ServiceRegistry.DOWNLOAD_REQUEST_MESSAGE_HANDLER);
	}
}