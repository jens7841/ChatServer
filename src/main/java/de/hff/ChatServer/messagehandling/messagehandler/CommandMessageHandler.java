package de.hff.ChatServer.messagehandling.messagehandler;

import de.hff.ChatServer.commandhandling.CommandHandler;
import de.hff.ChatServer.messagehandling.Message;
import de.hff.ChatServer.messagehandling.MessageType;
import de.hff.ChatServer.server.UserHandler;

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
