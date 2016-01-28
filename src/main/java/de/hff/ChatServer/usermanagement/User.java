package de.hff.ChatServer.usermanagement;

import de.hff.ChatShared.connectionhandling.Connection;
import de.hff.ChatShared.messagehandling.messageinput.MessageReceiver;
import de.hff.ChatShared.messagehandling.messageoutput.MessageSender;

public class User {

	private String name;
	private String password;
	private int id;

	private MessageSender messageSender;
	private MessageReceiver messageReceiver;
	private Connection connection;

	private int currentUploads;
	private int currentDownloads;

	public User(String name, String password, int id) {
		this.name = name;
		this.password = password;
		this.id = id;
	}

	public User() {
		this.name = "Not yet logged in";
		this.id = -100;
	}

	public User(String name, String password, int id, Connection connection, MessageReceiver messageReceiver,
			MessageSender messageSender) {
		this(name, password, id);
		this.connection = connection;
		this.messageReceiver = messageReceiver;
		this.messageSender = messageSender;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setMessageReceiver(MessageReceiver messageReceiver) {
		this.messageReceiver = messageReceiver;
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public MessageSender getMessageSender() {
		return messageSender;
	}

	public MessageReceiver getMessageReceiver() {
		return messageReceiver;
	}

	public int getCurrentDownloads() {
		return currentDownloads;
	}

	public int getCurrentUploads() {
		return currentUploads;
	}

	public void setCurrentDownloads(int currentDownloads) {
		this.currentDownloads = currentDownloads;
	}

	public void setCurrentUploads(int currentUploads) {
		this.currentUploads = currentUploads;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User u = (User) obj;
			if (u.getName().equals(getName()) && u.getPassword().equals(getPassword()))
				return true;
		}
		return false;
	}

}