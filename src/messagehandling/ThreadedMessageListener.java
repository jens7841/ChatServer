package messagehandling;

public class ThreadedMessageListener extends Thread implements MessageListener {

	private Connection connection;

	public ThreadedMessageListener(Connection con) {
		connection = con;
	}

	@Override
	public void run() {

	}

}
