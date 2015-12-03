package usermanagement;

public class UserAlreadyLoggedInException extends LoginException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5086620050436041961L;

	public UserAlreadyLoggedInException(String message) {
		super(message);
	}

}
