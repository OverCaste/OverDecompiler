package user.theovercaste.overdecompiler.parserdata.methodmembers;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodPrintingContext;

public class MethodActionGetThis extends MethodActionGetter {
    public MethodActionGetThis() {
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return "this";
    }

    @Override
    public ClassPath getClassType( ) {
        throw new UnsupportedOperationException("Can't get the class type of a 'this' keyword.");
    }
    
    @Override
    public boolean isForceInlined( ) {
        return true; //Always call 'this.something' instead of setting a variable to 'this' and then using that.
    }
}
