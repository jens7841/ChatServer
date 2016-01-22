package de.hff.ChatServer.usermanagement;

public class UserAlreadyExistsException extends UserException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4355323869177447601L;

	public UserAlreadyExistsException(String message) {
		super(message);
	}

}
