package de.hff.ChatServer.messagehandling.messagehandler;

import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatShared.messagehandling.Message;

public interface MessageHandler {

	public void handleMessage(Message message, UserHandler userHandler);

}