package de.hff.ChatServer.messagehandling;

import de.hff.ChatServer.messagehandling.messagehandler.MessageHandler;
import de.hff.ChatServer.server.ServiceRegistry;
import de.hff.ChatShared.messagehandling.MessageType;

public class MessageHandlerFactory {

	public static MessageHandler getMessageHandler(MessageType type) {

		switch (type) {
		case CHAT_MESSAGE:
			return (MessageHandler) ServiceRegistry.getService(ServiceRegistry.chatMessageHandler);

		case LOGIN:
			return (MessageHandler) ServiceRegistry.getService(ServiceRegistry.loginMessageHandler);

		case UPLOAD_PACKAGE:
			return (MessageHandler) ServiceRegistry.getService(ServiceRegistry.uploadPackageMessageHandler);

		case UPLOAD_REQUEST:
			return (MessageHandler) ServiceRegistry.getService(ServiceRegistry.uploadRequestMessageHandler);

		case DISCONNECT:
			return (MessageHandler) ServiceRegistry.getService(ServiceRegistry.disconnectMessageHandler);

		case COMMAND:
			return (MessageHandler) ServiceRegistry.getService(ServiceRegistry.commandMessageHandler);

		default:
			break;

		}
		return null;
	}
}