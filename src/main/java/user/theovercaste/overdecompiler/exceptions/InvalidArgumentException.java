package user.theovercaste.overdecompiler.exceptions;

public class InvalidArgumentException extends Exception {
	private static final long serialVersionUID = 7173035326071288548L;

	public InvalidArgumentException( ) {
		super();
	}

	public InvalidArgumentException(String message) {
		super(message);
	}

	public InvalidArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidArgumentException(Throwable cause) {
		super(cause);
	}
}
