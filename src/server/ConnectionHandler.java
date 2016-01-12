package server;

import messagehandling.messageinput.MessageListener;
import messagehandling.messageinput.ThreadedMessageListener;
import messagehandling.messageoutput.DefaultMessageSender;
import messagehandling.messageoutput.MessageSender;

public class ConnectionHandler {

	private Connection connection;
	private MessageListener messageListener;
	private MessageSender messageSender;

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

}
