package de.hff.ChatServer.usermanagement;

import de.hff.ChatServer.messagehandling.messageinput.ThreadedMessageListener;
import de.hff.ChatServer.messagehandling.messageoutput.DefaultMessageSender;
import de.hff.ChatServer.messagehandling.messageoutput.MessageSender;
import de.hff.ChatServer.server.Connection;

public class UserHandler {

	private User user;

	public UserHandler(Connection connection) {
		ThreadedMessageListener listener = new ThreadedMessageListener(this);
		MessageSender messageSender = new DefaultMessageSender(connection.getOutputstream());
		this.user = new User("", "", -100);
		this.user.setConnection(connection);
		this.user.setMessageListener(listener);
		this.user.setMessageSender(messageSender);
		listener.start();
	}

	public void setUser(User u) {
		user = u;
	}

	public User getUser() {
		return user;
	}

}
