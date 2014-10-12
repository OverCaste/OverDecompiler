package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public class MethodActionGetStaticField extends MethodActionGetter {
    private final String field;
    private final ClassPath staticClass;

    public MethodActionGetStaticField(String field, ClassPath staticClass) {
        this.field = field;
        this.staticClass = staticClass;
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent) {
        return staticClass.getClassName() + "." + field;
    }

    public String getField( ) {
        return field;
    }

    public ClassPath getStaticClass( ) {
        return staticClass;
    }
}
