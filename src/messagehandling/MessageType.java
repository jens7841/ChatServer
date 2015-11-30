package messagehandling;

import chatshared.Messages;

public enum MessageType {

	CHAT_MESSAGE(Messages.CHAT_MESSAGE), LOGIN(Messages.LOGIN), LOGIN_ERROR_MESSAGE(
			Messages.LOGIN_ERROR_MESSAGE), LOGIN_SUCCESS_MESSAGE(Messages.LOGIN_SUCCESS_MESSAGE), ERROR_MESSAGE(
					Messages.ERROR_MESSAGE), SUCCESS_MESSAGE(Messages.SUCCESS_MESSAGE), DISCONNECT(
							Messages.DISCONNECT), FILE(Messages.FILE), UPLOAD_REQUEST(
									Messages.UPLOAD_REQUEST), UPLOAD_CONFIRMATION(Messages.UPLOAD_CONFIRMATION);

	private int typeNumber;

	private MessageType(int typeNumber) {
		this.typeNumber = typeNumber;
	}

	public int getTypeNumber() {
		return typeNumber;
	}

	public static MessageType getType(int typeNumber) {
		for (MessageType msgType : values()) {
			if (msgType.getTypeNumber() == typeNumber) {
				return msgType;
			}
		}
		return null;
	}

}
