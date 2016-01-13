package messagehandling.messagehandler;

import messagehandling.Message;
import server.UserHandler;
import server.Service;

public interface MessageHandler extends Service {

	public void handleMessage(Message message, UserHandler connectionHandler);

}