package de.hff.ChatServer.messagehandling.messageoutput;

import java.io.IOException;
import java.io.OutputStream;

import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.messageoutput.MessageOutputstream;

public class DefaultMessageSender implements MessageSender {

	private OutputStream out;
	MessageOutputstream output;

	public DefaultMessageSender(MessageOutputstream out) {
		this.out = out;
		output = out;
	}

	@Override
	public synchronized void sendMessage(Message message) {

		try {

			output.writeMessage(message);
			output.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}