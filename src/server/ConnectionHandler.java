package server;

import messagehandling.messageinput.MessageListener;
import messagehandling.messageinput.ThreadedMessageListener;
import messagehandling.messageoutput.DefaultMessageSender;
import messagehandling.messageoutput.MessageSender;
import usermanagement.User;

public class ConnectionHandler {

	private Connection connection;
	private MessageListener messageListener;
	private MessageSender messageSender;
	private User user;

	public ConnectionHandler(Connection connection) {
		this.connection = connection;
		ThreadedMessageListener listener = new ThreadedMessageListener(this);
		listener.start();
		this.messageListener = listener;
		this.messageSender = new DefaultMessageSender(connection.getOutputstream());
	}

	public Connection getConnection() {
		return connection;
	}

	public MessageListener getMessageListener() {
		return messageListener;
	}

	public MessageSender getMessageSender() {
		return messageSender;
	}

	public void setUser(User u) {
		user = u;
	}

	public User getUser() {
		return user;
	}

}
