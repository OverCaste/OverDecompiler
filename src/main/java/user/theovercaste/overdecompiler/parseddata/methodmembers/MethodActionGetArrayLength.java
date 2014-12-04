package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

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
