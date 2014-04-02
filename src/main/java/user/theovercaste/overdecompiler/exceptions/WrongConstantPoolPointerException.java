package user.theovercaste.overdecompiler.exceptions;

public class WrongConstantPoolPointerException extends InvalidConstantPoolPointerException {
	private static final long serialVersionUID = 4389466630700087754L;

	public WrongConstantPoolPointerException( ) {
		super();
	}

	public WrongConstantPoolPointerException(String message) {
		super(message);
	}
}
