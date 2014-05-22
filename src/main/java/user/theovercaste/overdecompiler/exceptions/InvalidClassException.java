package user.theovercaste.overdecompiler.exceptions;

public class InvalidClassException extends Exception {
    private static final long serialVersionUID = -7027425099191368378L;

    public InvalidClassException( ) {
        super();
    }

    public InvalidClassException(String message) {
        super(message);
    }

    public InvalidClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidClassException(Throwable cause) {
        super(cause);
    }
}
