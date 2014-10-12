package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public abstract class MethodAction extends MethodMember {
    public MethodAction( ) {
        super(Type.ACTION);
    }

    public abstract String getStringValue(ParsedClass c, ParsedMethod parent);
}
