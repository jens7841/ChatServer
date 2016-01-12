package messagehandling;

import server.Service;

public interface MessageHandler extends Service {

	public void handleMessage(Message message);

}