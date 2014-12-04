package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

public class MethodActionLoadParameter extends MethodActionLoadVariable {
    public MethodActionLoadParameter(int index, ClassPath type) {
        super(index, type);
    }

    @Override
    public String getStringValue(final ParsedClass c, final ParsedMethod parent, MethodPrintingContext ctx) {
        return ctx.getVariableName(index, true, getClassType());
    }

    @Override
    public boolean isForceInlined( ) {
        return true;
    }
}
