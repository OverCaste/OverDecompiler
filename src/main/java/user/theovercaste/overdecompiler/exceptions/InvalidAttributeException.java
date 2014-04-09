package user.theovercaste.overdecompiler.exceptions;

public class InvalidAttributeException extends RuntimeException {
	private static final long serialVersionUID = -8768866280526920613L;

	public InvalidAttributeException( ) {
		super();
	}

	public InvalidAttributeException(String message) {
		super(message);
	}

	public InvalidAttributeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAttributeException(Throwable cause) {
		super(cause);
	}
}
