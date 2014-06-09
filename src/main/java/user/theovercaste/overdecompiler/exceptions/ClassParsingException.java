package user.theovercaste.overdecompiler.exceptions;

public class ClassParsingException extends Exception {
    private static final long serialVersionUID = 2573710756020425552L;

    public ClassParsingException( ) {
        super();
    }

    public ClassParsingException(String message) {
        super(message);
    }

    public ClassParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassParsingException(Throwable cause) {
        super(cause);
    }
}
