package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

public class MethodActionLoadVariable extends MethodActionGetter {
    protected final int index;
    protected final ClassPath type;

    public MethodActionLoadVariable(int index, ClassPath type) {
        this.index = index;
        this.type = type;
    }

    @Override
    public String getStringValue(final ParsedClass c, final ParsedMethod parent, MethodPrintingContext ctx) {
        return ctx.getVariableName(index, false, getClassType());
    }

    public int getVariableIndex( ) {
        return index;
    }

    @Override
    public ClassPath getClassType( ) {
        return type;
    }

    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        // Do nothing
    }
}
