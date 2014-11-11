package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodPrintingContext;

public abstract class MethodAction extends MethodMember {
    public MethodAction( ) {
        super(Type.ACTION);
    }

    public abstract String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx);

    public abstract ClassPath getClassType( );

    /** Whether or not this action pushes a value onto the stack. Methods which return void wouldn't, for example. */
    public boolean isGetter( ) {
        return false;
    }
    
    /** If this action should be inlined regardless of how many references it has.*/
    public boolean isForceInlined( ) {
        return false;
    }
}
