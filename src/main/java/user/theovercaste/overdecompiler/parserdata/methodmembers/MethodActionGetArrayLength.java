package user.theovercaste.overdecompiler.parserdata.methodmembers;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodPrintingContext;

public class MethodActionGetArrayLength extends MethodActionGetter {
    private final MethodActionPointer array;

    public MethodActionGetArrayLength(MethodActionPointer array) {
        this.array = array;
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return array.get(ctx).getStringValue(c, parent, ctx) + ".length";
    }

    public MethodActionPointer getArray( ) {
        return array;
    }

    @Override
    public ClassPath getClassType( ) {
        return ClassPath.INTEGER;
    }
}
