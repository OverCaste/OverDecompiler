package user.theovercaste.overdecompiler.exceptions;

import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodMember;

public class InvalidStackTypeException extends InstructionParsingException {
    private static final long serialVersionUID = 2983302269144247035L;

    public InvalidStackTypeException(MethodMember a) {
        super("The instruction which owns this invoker isn't a proper type! (" + a.getClass().getName() + ")");
    }
}
