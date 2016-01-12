package messagehandling;

import java.io.InputStream;

public class ThreadedMessageListener extends Thread implements MessageListener {

	private InputStream in;

	public ThreadedMessageListener(InputStream in) {
		this.in = in;
	}

	@Override
	public void run() {

	}

}
