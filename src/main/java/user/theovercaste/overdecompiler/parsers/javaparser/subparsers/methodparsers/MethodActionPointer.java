package user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers;

import user.theovercaste.overdecompiler.parseddata.methodmembers.*;

/**
 * A pointer to a specific MethodActionGetter, these are pushed onto a stack by instructions, and then read for variable parsing.
 * 
 * @author <a href="http://www.reddit.com/user/TheOverCaste/">OverCaste</a>
 *
 */
public class MethodActionPointer {
    protected final int index;

    MethodActionPointer(int index) {
        this.index = index;
    }

    public MethodAction get(MethodPrintingContext printingContext) {
        MethodMember member = printingContext.getMember(index);
        if (member instanceof MethodAction) {
            MethodAction action = (MethodAction) member;
            MethodAction baseAction = (action instanceof MethodActionSetVariable) ? ((MethodActionSetVariable) action).getExpression() : action; // Since we force every getter into a MethodActionSetVariable, in order to retrieve the 'base' type, we just get the 'setter' or the 'expression.'
            if (baseAction.isForceInlined() || printingContext.isReferenceInlined(index)) {
                return baseAction;
            }
            return new MethodActionLoadVariable(index, action.getClassType());
        } else {
            // TODO actual exception
            throw new RuntimeException("There was a pointer to a method action that wasn't a getter?"); // There should only ever be MethodActionPointers to the member list where the member is a MethodAction.
        }
    }

    @Override
    public String toString( ) {
        return "[Method action pointer to index " + index + "]";
    }

    @Override
    public int hashCode( ) {
        return 37 * index;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MethodActionPointer) {
            return other.getClass().equals(getClass()) && ((MethodActionPointer) other).index == index;
        }
        return false;
    }
}
