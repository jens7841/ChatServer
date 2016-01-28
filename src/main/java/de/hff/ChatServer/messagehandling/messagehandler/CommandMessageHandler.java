package de.hff.ChatServer.messagehandling.messagehandler;

import java.io.IOException;

import de.hff.ChatServer.commandhandling.CommandHandler;
import de.hff.ChatServer.usermanagement.UserHandler;
import de.hff.ChatShared.messagehandling.Message;
import de.hff.ChatShared.messagehandling.MessageHandler;
import de.hff.ChatShared.messagehandling.MessageType;

public class CommandMessageHandler implements MessageHandler {

	private CommandHandler commandHandler;
	private UserHandler userHandler;

	public CommandMessageHandler(CommandHandler commandHandler, UserHandler userHandler) {
		this.commandHandler = commandHandler;
		this.userHandler = userHandler;
	}

	@Override
	public void receiveMessage(Message message) {
		if (message.getType() == MessageType.COMMAND) {
			handleMessage(message);
		}
	}

	@Override
	public void handleMessage(Message message) {
		if (commandHandler.commandKnown(message.toString())) {
			commandHandler.handleCommand(message.toString());
		} else {
			try {
				userHandler.getUser().getMessageSender()
						.sendMessage(new Message("-> Command nicht bekannt!", MessageType.ERROR));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
