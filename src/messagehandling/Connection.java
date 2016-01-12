package messagehandling;

public class Connection {

	private MessageListener listener;
	private IMessageSender sender;

	public Connection(MessageListener listener, IMessageSender sender) {
		this.listener = listener;
		this.sender = sender;
	}

	public MessageListener getListener() {
		return listener;
	}

	public IMessageSender getSender() {
		return sender;
	}
}