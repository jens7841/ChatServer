package de.hff.ChatServer.messagehandling.messageoutput;

import de.hff.ChatServer.messagehandling.Message;

public interface MessageSender {

	public void sendMessage(Message message);
}