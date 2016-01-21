package chatserver.messagehandling.messagehandler;

import chatserver.commandhandling.CommandHandler;
import chatserver.messagehandling.Message;
import chatserver.messagehandling.MessageType;
import chatserver.server.UserHandler;

public class CommandMessageHandler implements MessageHandler {

	private CommandHandler commandHandler;

	public CommandMessageHandler(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}

	@Override
	public void handleMessage(Message message, UserHandler userHandler) {
		if (commandHandler.commandKnown(message.toString())) {
			commandHandler.handleCommand(message.toString());
		} else {
			System.out.println("Command nicht bekannt");
			userHandler.getUser().getMessageSender()
					.sendMessage(new Message("-> Command nicht bekannt!", MessageType.ERROR));
		}
	}

}
