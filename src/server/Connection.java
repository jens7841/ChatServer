package server;

import messagehandling.MessageInputStream;
import messagehandling.MessageOutputstream;

public class Connection {

	private MessageOutputstream out;
	private MessageInputStream in;

	public Connection(MessageOutputstream out, MessageInputStream in) {
		this.out = out;
		this.in = in;
	}

	public MessageInputStream getInputstream() {
		return in;
	}

	public MessageOutputstream getOutputstream() {
		return out;
	}

}