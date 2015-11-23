package usermanager;

public class UserAlreadyLoggedInException extends UserException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1489464400258855633L;

	public UserAlreadyLoggedInException(String msg) {
		super(msg);
	}

	public UserAlreadyLoggedInException() {
		super();
	}
}
