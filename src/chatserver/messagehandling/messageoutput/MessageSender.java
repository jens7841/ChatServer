package chatserver.messagehandling.messageoutput;

import chatserver.messagehandling.Message;

public interface MessageSender {

	public void sendMessage(Message message);
}