package de.hff.ChatServer.messagehandling;

import de.hff.ChatServer.messagehandling.messagehandler.MessageHandler;
import de.hff.ChatServer.server.ServiceRegistry;
import de.hff.ChatShared.messagehandling.MessageType;

public class MessageHandlerFactory {

	public static MessageHandler getMessageHandler(MessageType type) {

		switch (type) {
		case CHAT_MESSAGE:
			return ServiceRegistry.getService(ServiceRegistry.CHAT_MESSAGE_HANDLER, MessageHandler.class);

		case LOGIN:
			return ServiceRegistry.getService(ServiceRegistry.LOGIN_MESSAGE_HANDLER, MessageHandler.class);

		case UPLOAD_PACKAGE:
			return ServiceRegistry.getService(ServiceRegistry.UPLOAD_PACKAGE_MESSAGE_HANDLER, MessageHandler.class);

		case UPLOAD_REQUEST:
			return ServiceRegistry.getService(ServiceRegistry.UPLOAD_REQUEST_MESSAGE_HANDLER, MessageHandler.class);

		case DISCONNECT:
			return ServiceRegistry.getService(ServiceRegistry.DISCONNECT_MESSAGE_HANDLER, MessageHandler.class);

		case COMMAND:
			return ServiceRegistry.getService(ServiceRegistry.COMMAND_MESSAGE_HANDLER, MessageHandler.class);

		case DOWNLOAD_REQUEST:
			return ServiceRegistry.getService(ServiceRegistry.DOWNLOAD_REQUEST_MESSAGE_HANDLER, MessageHandler.class);

		default:
			break;

		}
		return null;
	}
}