package user.theovercaste.overdecompiler.parsers;

import java.util.EnumSet;

import user.theovercaste.overdecompiler.codeinternals.*;
import user.theovercaste.overdecompiler.exceptions.ClassParsingException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;

public abstract class AbstractParser implements ClassParser {
    protected ParsedClass parsedClass;

    protected abstract ClassPath getClassPath( ) throws ClassParsingException;

    protected abstract ClassType getClassType( ) throws ClassParsingException;

    protected abstract ClassPath getParentPath( ) throws ClassParsingException;

    protected abstract void parseInterfaces( ) throws ClassParsingException;

    protected abstract void parseFields( ) throws ClassParsingException;

    protected abstract void parseMethods( ) throws ClassParsingException;

    protected abstract void parseAnnotations( ) throws ClassParsingException;

    protected abstract void parseFlags( ) throws ClassParsingException;

    protected void addImport(ClassPath i) {
        if (i.isObject() && !"java.lang".equals(i.getClassPackage())) {
            parsedClass.addImport(i);
        }
    }

    public EnumSet<ClassFlag> getClassFlags(int bitmask) {
        EnumSet<ClassFlag> ret = EnumSet.noneOf(ClassFlag.class);
        for (ClassFlag f : ClassFlag.values()) {
            if ((bitmask & f.getOpcode()) != 0) {
                ret.add(f);
            }
        }
        return ret;
    }

    public EnumSet<MethodFlag> getMethodFlags(int bitmask) {
        EnumSet<MethodFlag> ret = EnumSet.noneOf(MethodFlag.class);
        for (MethodFlag f : MethodFlag.values()) {
            if ((bitmask & f.getOpcode()) != 0) {
                ret.add(f);
            }
        }
        return ret;
    }

    public EnumSet<FieldFlag> getFieldFlags(int bitmask) {
        EnumSet<FieldFlag> ret = EnumSet.noneOf(FieldFlag.class);
        for (FieldFlag f : FieldFlag.values()) {
            if ((bitmask & f.getOpcode()) != 0) {
                ret.add(f);
            }
        }
        return ret;
    }

    @Override
    public ParsedClass parseClass( ) throws ClassParsingException {
        ClassPath parsedClassPath = getClassPath();
        parsedClass = new ParsedClass(parsedClassPath.getClassName(), parsedClassPath.getClassPackage(), getClassType(), getParentPath());
        parseInterfaces();
        parseFields();
        parseMethods();
        parseAnnotations();
        parseFlags();
        return parsedClass;
    }
}
