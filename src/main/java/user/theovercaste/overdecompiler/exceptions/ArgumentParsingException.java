package user.theovercaste.overdecompiler.exceptions;

public class ArgumentParsingException extends Exception {
    private static final long serialVersionUID = -3748219092206256902L;

    public ArgumentParsingException( ) {
        super();
    }

    public ArgumentParsingException(String message) {
        super(message);
    }

    public ArgumentParsingException(Throwable cause) {
        super(cause);
    }

    public ArgumentParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
