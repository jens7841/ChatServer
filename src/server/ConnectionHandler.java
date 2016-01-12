package server;

import messagehandling.messageinput.MessageListener;
import messagehandling.messageinput.ThreadedMessageListener;
import messagehandling.messageoutput.DefaultMessageSender;
import messagehandling.messageoutput.MessageSender;

public class ConnectionHandler {

	public static long counter = Long.MIN_VALUE;

	private Connection connection;
	private MessageListener messageListener;
	private MessageSender messageSender;
	private long id;

	public ConnectionHandler(Connection connection) {
		id = counter;
		counter++;
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConnectionHandler) {
			if (((ConnectionHandler) obj).id == id) {
				return true;
			}
		}
		return false;
	}

}
