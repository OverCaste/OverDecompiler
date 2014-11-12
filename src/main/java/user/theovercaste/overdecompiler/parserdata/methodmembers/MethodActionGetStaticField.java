package user.theovercaste.overdecompiler.parserdata.methodmembers;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodPrintingContext;

public class MethodActionGetStaticField extends MethodActionGetter {
    private final String field;
    private final ClassPath staticClass;
    private final ClassPath fieldClass;

    public MethodActionGetStaticField(String field, ClassPath staticClass, ClassPath fieldClass) {
        this.field = field;
        this.staticClass = staticClass;
        this.fieldClass = fieldClass;
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return staticClass.getClassName() + "." + field;
    }

    public String getField( ) {
        return field;
    }

    public ClassPath getStaticClass( ) {
        return staticClass;
    }

    @Override
    public ClassPath getClassType( ) {
        return fieldClass;
    }

    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        //do nothing
    }
    
    @Override
    public String toString( ) {
        return "get field " + staticClass.getFullDefinition() + "." + field + " type: " + fieldClass.getFullDefinition();
    }
}
