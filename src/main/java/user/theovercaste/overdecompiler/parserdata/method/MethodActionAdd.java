package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodPrintingContext;

public class MethodActionAdd extends MethodActionGetter {
    private final MethodActionPointer valueOne;
    private final MethodActionPointer valueTwo;
    private final ClassPath returnType;

    /**
     * A method action which adds two numeric types. The values' types should match, and be identical to the returnType.
     * 
     * @param valueOne the first [left] variable to be added.
     * @param valueTwo the second [right] variable to be added.
     * @param returnType the type that this method action will push onto the stack. Usually the type of the first value.
     */
    public MethodActionAdd(MethodActionPointer valueOne, MethodActionPointer valueTwo, ClassPath returnType) {
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
        this.returnType = returnType;
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return valueOne.get(ctx).getStringValue(c, parent, ctx) + " + " + valueTwo.get(ctx).getStringValue(c, parent, ctx);
    }

    @Override
    public ClassPath getClassType( ) {
        return returnType;
    }
}
