package user.theovercaste.overdecompiler.exceptions;

public class InstructionParsingException extends Exception {
    private static final long serialVersionUID = 739747142315853439L;

    public InstructionParsingException( ) {
        super();
    }

    public InstructionParsingException(String message) {
        super(message);
    }

    public InstructionParsingException(Throwable cause) {
        super(cause);
    }

    public InstructionParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
