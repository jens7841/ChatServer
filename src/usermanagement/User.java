package usermanagement;

import messagehandling.messageinput.MessageListener;
import messagehandling.messageoutput.MessageSender;
import server.Connection;

public class User {

	private String name;
	private String password;
	private int id;
	private int simultaneosUploads = 0;

	private MessageSender messageSender;
	private MessageListener messageListener;
	private Connection connection;

	public User(String name, String password, int id) {
		this.name = name;
		this.password = password;
		this.id = id;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public MessageListener getMessageListener() {
		return messageListener;
	}

	public MessageSender getMessageSender() {
		return messageSender;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public int getID() {
		return id;
	}

	public int getSimultaneosUploads() {
		return simultaneosUploads;
	}

	public void setSimultaneosUploads(int simultaneosUploads) {
		this.simultaneosUploads = simultaneosUploads;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof User) {
			User user = (User) obj;
			if (user.getName().equals(this.getName()) && user.getPassword().equals(this.getPassword())) {
				return true;
			}
		}

		return false;
	}

}