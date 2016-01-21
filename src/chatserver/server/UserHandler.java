package chatserver.server;

import chatserver.messagehandling.messageinput.ThreadedMessageListener;
import chatserver.messagehandling.messageoutput.DefaultMessageSender;
import chatserver.messagehandling.messageoutput.MessageSender;
import chatserver.usermanagement.User;

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
