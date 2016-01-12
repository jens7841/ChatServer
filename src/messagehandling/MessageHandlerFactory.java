package messagehandling;

import server.ServiceRegistry;

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

		default:
			break;

		}
		return null;
	}
}