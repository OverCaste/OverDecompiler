package user.theovercaste.overdecompiler.exceptions;

public class InvalidConstantPoolPointerException extends Exception {
    private static final long serialVersionUID = 4389466630700087754L;

    public InvalidConstantPoolPointerException( ) {
        super();
    }

    public InvalidConstantPoolPointerException(String message) {
        super(message);
    }

    public InvalidConstantPoolPointerException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConstantPoolPointerException(Throwable cause) {
        super(cause);
    }
}
