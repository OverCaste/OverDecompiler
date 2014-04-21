package user.theovercaste.overdecompiler.exceptions;

public class EndOfStackException extends InstructionParsingException {
	private static final long serialVersionUID = 2983302269144247035L;

	public EndOfStackException( ) {
		super("Unexpectedly reached end of stack.");
	}
}
