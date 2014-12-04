package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

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
