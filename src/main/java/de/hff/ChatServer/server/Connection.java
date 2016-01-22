package de.hff.ChatServer.server;

import de.hff.ChatShared.messagehandling.messageinput.MessageInputStream;
import de.hff.ChatShared.messagehandling.messageoutput.MessageOutputstream;

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