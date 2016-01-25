package de.hff.ChatServer.messagehandling.messagehandler;

import de.hff.ChatServer.server.Service;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatShared.messagehandling.Message;

public interface MessageHandler extends Service {

	public void handleMessage(Message message, UserHandler userHandler);

}