package chatserver.messagehandling.messagehandler;

import chatserver.messagehandling.Message;
import chatserver.server.Service;
import chatserver.server.UserHandler;

public interface MessageHandler extends Service {

	public void handleMessage(Message message, UserHandler userHandler);

}