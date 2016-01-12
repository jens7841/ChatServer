package messagehandling;

import java.io.DataInputStream;
import java.io.InputStream;

public class MessageListener extends Thread {

	protected DataInputStream in;

	public MessageListener(InputStream in) {
		this.in = new DataInputStream(in);
	}

	public InputStream getInputstream() {
		return in;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof MessageListener) {
			MessageListener listener = (MessageListener) object;
			if (listener.getInputstream().equals(getInputstream())) {
				return true;
			}
		}
		return false;
	}

}
