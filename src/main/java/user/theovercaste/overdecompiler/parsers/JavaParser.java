package user.theovercaste.overdecompiler.parsers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import user.theovercaste.overdecompiler.attributes.AttributableElement;
import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.attributes.RuntimeVisibleAnnotationsAttribute;
import user.theovercaste.overdecompiler.codeinternals.ClassFlag;
import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.ClassType;
import user.theovercaste.overdecompiler.codeinternals.FieldFlag;
import user.theovercaste.overdecompiler.codeinternals.MethodFlag;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.FieldData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.ClassParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.ParsedField;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.JavaMethodParser;

import com.google.common.base.Preconditions;

public class JavaParser extends AbstractParser {
    protected static final JavaMethodParser methodParser = new JavaMethodParser();

    protected final ClassData classData;

    public JavaParser(ClassData classData) {
        Preconditions.checkNotNull(classData);
        this.classData = classData;
    }

    @Override
    public ClassType getClassType( ) {
        EnumSet<ClassFlag> classFlags = getClassFlags(classData.getFlagMask());
        if (classFlags.contains(ClassFlag.ENUM)) {
            return ClassType.ENUM;
        }
        if (classFlags.contains(ClassFlag.ANNOTATION)) {
            return ClassType.ANNOTATION;
        }
        if (classFlags.contains(ClassFlag.INTERFACE)) {
            return ClassType.INTERFACE;
        }
        if (classFlags.contains(ClassFlag.ABSTRACT)) {
            return ClassType.ABSTRACT_CLASS;
        }
        return ClassType.CLASS;
    }

    public Collection<ClassPath> getAnnotations(AttributableElement e) {
        ArrayList<ClassPath> ret = new ArrayList<ClassPath>(5); // You'll very rarely get > 5 annotations
        for (AttributeData d : e.getAttributes()) {
            try {
                if ("RuntimeVisibleAnnotations".equals(d.getName(classData.getConstantPool()))) {
                    RuntimeVisibleAnnotationsAttribute attribute = RuntimeVisibleAnnotationsAttribute.parser().parse(d, classData.getConstantPool());
                    for (RuntimeVisibleAnnotationsAttribute.Annotation a : attribute.getAnnotations()) {
                        // TODO finish annotation
                    }
                }
            } catch (InvalidConstantPoolPointerException ex) { // Just because we have one invalid annotation doesn't mean they're all invalid.
                ex.printStackTrace();
            } catch (InvalidAttributeException e1) { // ^
                e1.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    public ClassPath getParentPath( ) throws ClassParsingException {
        try {
            return new ClassPath(classData.getConstantPool().getClassName(classData.getParentId()));
        } catch (InvalidConstantPoolPointerException ex) {
            throw new ClassParsingException("Failed to get class parent.", ex);
        }
    }

    @Override
    public ClassPath getClassPath( ) throws ClassParsingException {
        try {
            return new ClassPath(classData.getConstantPool().getClassName(classData.getClassId()));
        } catch (InvalidConstantPoolPointerException ex) {
            throw new ClassParsingException("Failed to get classpath.", ex);
        }
    }

    @Override
    public void parseInterfaces( ) throws ClassParsingException {
        ConstantPool constantPool = classData.getConstantPool();
        try {
            for (int i : classData.getInterfaces()) {
                ClassPath classPath = new ClassPath(constantPool.getClassName(i));
                addImport(classPath);
                parsedClass.addInterface(classPath);
            }
        } catch (InvalidConstantPoolPointerException ex) {
            throw new ClassParsingException("Failed to parse interfaces.", ex);
        }
    }

    @Override
    public void parseFields( ) throws ClassParsingException {
        ConstantPool constantPool = classData.getConstantPool();
        try {
            for (FieldData f : classData.getFields()) {
                ClassPath classPath = ClassPath.getMangledPath(f.getDescription(constantPool));
                addImport(classPath);
                ParsedField parsed = new ParsedField(classPath, f.getName(constantPool));
                for (FieldFlag flag : getFieldFlags(f.getFlagMask())) {
                    parsed.addFlag(flag); // TODO eventually change this to .addAll or something
                }
                parsedClass.addField(parsed);
            }
        } catch (InvalidConstantPoolPointerException ex) {
            throw new ClassParsingException("Failed to parse fields.", ex);
        }
    }

    @Override
    public void parseMethods( ) throws ClassParsingException {
        try {
            for (MethodData m : classData.getMethods()) {
                parsedClass.addMethod(parseMethod(m));
            }
        } catch (InvalidConstantPoolPointerException ex) {
            throw new ClassParsingException("Failed to parse fields.", ex);
        }
    }

    @Override
    protected void parseAnnotations( ) throws ClassParsingException {
        // TODO
    }

    public ParsedMethod parseMethod(MethodData m) throws InvalidConstantPoolPointerException {
        String descriptor = m.getDescription(classData.getConstantPool());
        ClassPath returnClassPath = ClassPath.getMethodReturnType(descriptor);
        addImport(returnClassPath);
        ParsedMethod parsed = new ParsedMethod(returnClassPath, m.getName(classData.getConstantPool()));
        for (ClassPath arg : ClassPath.getMethodArguments(descriptor)) {
            addImport(arg);
            parsed.addArgument(arg);
        }
        for (MethodFlag flag : getMethodFlags(m.getFlagMask())) {
            parsed.addFlag(flag); // TODO eventually change this to .addAll or something
        }
        try {
            methodParser.parseMethodActions(classData, parsedClass, m, parsed);
        } catch (InvalidAttributeException e) {
            e.printStackTrace();
        }
        return parsed;
    }
}
