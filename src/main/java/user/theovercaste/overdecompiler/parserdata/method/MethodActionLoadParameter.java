package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodPrintingContext;

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
