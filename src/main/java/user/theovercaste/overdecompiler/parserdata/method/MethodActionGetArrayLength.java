package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public class MethodActionGetArrayLength extends MethodActionGetter {
    private final MethodActionGetter array;

    public MethodActionGetArrayLength(MethodActionGetter array) {
        this.array = array;
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent) {
        return array.getStringValue(c, parent) + ".length";
    }

    public MethodActionGetter getArray( ) {
        return array;
    }
}
