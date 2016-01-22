package de.hff.ChatServer.messagehandling.messagehandler;

import de.hff.ChatServer.commandhandling.CommandHandler;
import de.hff.ChatServer.server.UserHandler;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageType;

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
