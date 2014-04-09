package user.theovercaste.overdecompiler.exceptions;

public class InvalidConstantPoolPointerIndexException extends InvalidConstantPoolPointerException {
	private static final long serialVersionUID = 4389466630700087754L;

	public InvalidConstantPoolPointerIndexException( ) {
		super();
	}

	public InvalidConstantPoolPointerIndexException(String message) {
		super(message);
	}

	public InvalidConstantPoolPointerIndexException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidConstantPoolPointerIndexException(Throwable cause) {
		super(cause);
	}
}
