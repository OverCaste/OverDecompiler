package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodPrintingContext;

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
        //Do nothing
    }
}
