package messagehandling.messagehandler;

import messagehandling.Message;
import server.Connection;
import server.Service;

public interface MessageHandler extends Service {

	public void handleMessage(Message message, Connection connection);

}