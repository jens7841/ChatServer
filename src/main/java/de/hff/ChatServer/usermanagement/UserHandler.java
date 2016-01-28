package de.hff.ChatServer.usermanagement;

import de.hff.ChatShared.connectionhandling.Connection;
import de.hff.ChatShared.messagehandling.messageinput.MessageReceiver;
import de.hff.ChatShared.messagehandling.messageoutput.MessageSender;

public class UserHandler {

	private User user;

	public UserHandler(Connection connection) {
		MessageReceiver messageReceiver = new MessageReceiver(connection.getInputstream());

		MessageSender messageSender = new MessageSender(connection.getOutputstream());

		this.user = new User("", "", -100, connection, messageReceiver, messageSender);

	}

	public void setUser(User u) {
		user = u;
	}

	public User getUser() {
		return user;
	}

}