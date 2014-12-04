package user.theovercaste.overdecompiler.parseddata.methodmembers;

import java.util.Arrays;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

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
        // do nothing
    }

    @Override
    public String toString( ) {
        return "get field " + staticClass.getFullDeclaration() + "." + field + " type: " + fieldClass.getFullDeclaration();
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Arrays.asList(staticClass); // We don't need to import the fieldClass because that is transparent in the source code.
                                           // java.lang.System.out.println(...), not java.lang.System.out(java.io.PrintStream).println(...)
    }
}
