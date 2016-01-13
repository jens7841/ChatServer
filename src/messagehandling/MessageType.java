package messagehandling;

public enum MessageType {

	CHAT_MESSAGE(0), LOGIN(1), LOGIN_ERROR_MESSAGE(2), LOGIN_SUCCESS_MESSAGE(3), ERROR_MESSAGE(4), SUCCESS_MESSAGE(
			5), DISCONNECT(6), UPLOAD_REQUEST(7), UPLOAD_CONFIRMATION(8), UPLOAD_PACKAGE(9), UPLOAD_REJECT(
					10), COMMAND_MESSAGE(11);

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

	/*
	 * 
	 * Upload Reject:
	 * 
	 * UPReject FilenameLength(int) Filename Filesize(long)
	 * 
	 * 
	 * Upload Request:
	 * 
	 * UPRequest FilenameLength(int) Filename Filesize(long)
	 * 
	 * Upload Confirmation
	 * 
	 * UPConfirm FilenameLeng(int) FileName FileSize(long) id(int)
	 * 
	 * Datei Sendeprotokoll:
	 * 
	 * FileBegin packLeng FileID(INT) dataFullLeng(LONG) fileNameLeng(INT)
	 * fileName dataLeng(INT) data
	 * 
	 * FilePack packLeng FileID dataLeng data
	 * 
	 * 
	 * 
	 */

}
