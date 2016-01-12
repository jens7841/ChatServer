package messagehandling.messagehandler;

import java.io.UnsupportedEncodingException;

import messagehandling.Message;
import server.ConnectionHandler;
import usermanagement.User;

public class ChatMessageHandler implements MessageHandler {

	@Override
	public void handleMessage(Message message, ConnectionHandler connectionHandler) {
		try {
			User user = connectionHandler.getUser();
			if (user != null) {
				System.out.println("> " + user.getName() + ": " + new String(message.getBytes(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
