package messagehandling;

import server.ServiceRegistry;

public class MessageHandlerFactory {

	public static MessageHandler getMessageHandler(MessageType type) {

		switch (type) {
		case CHAT_MESSAGE:
			return ServiceRegistry.getMessageHandler(ServiceRegistry.chatMessageHandler);

		case LOGIN:
			return ServiceRegistry.getMessageHandler(ServiceRegistry.loginMessageHandler);

		case UPLOAD_PACKAGE:
			return ServiceRegistry.getMessageHandler(ServiceRegistry.uploadPackageMessageHandler);

		case UPLOAD_REQUEST:
			return ServiceRegistry.getMessageHandler(ServiceRegistry.uploadRequestMessageHandler);

		default:
			break;

		}
		return null;
	}
}
