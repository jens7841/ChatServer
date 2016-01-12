package messagehandling.messagehandler;

import messagehandling.Message;
import server.ConnectionHandler;
import server.Service;

public interface MessageHandler extends Service {

	public void handleMessage(Message message, ConnectionHandler connectionHandler);

}