package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

public abstract class MethodAction extends MethodMember {
    public MethodAction( ) {
        super(Type.ACTION);
    }

    public abstract String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx);

    public ClassPath getClassType( ) {
        throw new UnsupportedOperationException("Can't get the type of a class which doesn't push a value onto the stack!");
    }

    /** Whether or not this action pushes a value onto the stack. Methods which return void wouldn't, for example. */
    public boolean isGetter( ) {
        return false;
    }

    /** If this action should be inlined regardless of how many references it has. */
    public boolean isForceInlined( ) {
        return false;
    }
}
