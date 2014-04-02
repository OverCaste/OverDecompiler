package user.theovercaste.overdecompiler.exceptions;

public class InvalidInstructionException extends RuntimeException {
	private static final long serialVersionUID = -8768866280526920613L;

	public InvalidInstructionException( ) {
		super();
	}

	public InvalidInstructionException(String message) {
		super(message);
	}
}
