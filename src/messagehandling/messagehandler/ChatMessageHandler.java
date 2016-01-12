package messagehandling.messagehandler;

import java.io.UnsupportedEncodingException;

import messagehandling.Message;
import server.ConnectionHandler;

public class ChatMessageHandler implements MessageHandler {

	@Override
	public void handleMessage(Message message, ConnectionHandler connectionHandler) {
		try {
			System.out.println(new String(message.getBytes(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
