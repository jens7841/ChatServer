package de.hff.ChatServer.messagehandling.messagehandler;

import de.hff.ChatServer.messagehandling.Message;
import de.hff.ChatServer.server.Service;
import de.hff.ChatServer.server.UserHandler;

public interface MessageHandler extends Service {

	public void handleMessage(Message message, UserHandler userHandler);

}