package user.theovercaste.overdecompiler.exceptions;

public class InvalidConstantPoolEntryException extends Exception {
    private static final long serialVersionUID = 5087982751640360453L;

    public InvalidConstantPoolEntryException( ) {
        super();
    }

    public InvalidConstantPoolEntryException(String message) {
        super(message);
    }

    public InvalidConstantPoolEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConstantPoolEntryException(Throwable cause) {
        super(cause);
    }
}
