package user.theovercaste.overdecompiler.exceptions;

public class InvalidConstantPoolPointerIndexException extends InvalidConstantPoolPointerException {
	private static final long serialVersionUID = 4389466630700087754L;

	public InvalidConstantPoolPointerIndexException( ) {
		super();
	}

	public InvalidConstantPoolPointerIndexException(String message) {
		super(message);
	}
}
