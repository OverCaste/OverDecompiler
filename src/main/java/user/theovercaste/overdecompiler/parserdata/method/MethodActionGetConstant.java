package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodPrintingContext;

import com.google.common.base.Preconditions;

public class MethodActionGetConstant extends MethodActionGetter {
    public enum ConstantType {
        CHAR(ClassPath.CHAR),
        BYTE(ClassPath.BYTE),
        SHORT(ClassPath.SHORT),
        INT(ClassPath.INTEGER),
        LONG(ClassPath.LONG),
        FLOAT(ClassPath.FLOAT),
        DOUBLE(ClassPath.DOUBLE),
        STRING(ClassPath.OBJECT_STRING),
        CLASS(ClassPath.OBJECT_CLASS),
        NULL(ClassPath.OBJECT);

        private final ClassPath classType;

        ConstantType(ClassPath classType) {
            this.classType = classType;
        }
    }

    private String value;
    private ConstantType type;

    public MethodActionGetConstant(String value, ConstantType type) {
        Preconditions.checkNotNull(type);
        if (type == ConstantType.NULL) {
            Preconditions.checkArgument(value == null, "If type is 'null', value must be null!");
        }
        this.value = value;
        this.type = type;
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        switch (type) {
            case CHAR:
                return "'" + value + "'";
            case BYTE:
                return "(byte)" + value;
            case SHORT:
                return value + "s";
            case INT:
                return value;
            case LONG:
            case DOUBLE:
                return value + "l";
            case FLOAT:
                return value + "f";
            case STRING:
                return "\"" + value + "\"";
            case CLASS:
                return value.replace("/", ".") + ".class";
            case NULL:
                return "null";
        }
        return "<ERROR INVALID TYPE: " + type.name() + "-" + value + ">"; // Try to decompile even if we fail.
    }

    public String getValue( ) {
        return value;
    }

    public ConstantType getConstantType( ) {
        return type;
    }

    @Override
    public ClassPath getClassType( ) {
        return type.classType;
    }
    
    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        //Do nothing
    }
    
    @Override
    public boolean isForceInlined( ) {
        return true;
    }
    
    @Override
    public String toString( ) {
        return "load constant " + getStringValue(null, null, null);
    }
}
