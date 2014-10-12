package user.theovercaste.overdecompiler.parsers;

import java.util.*;

import user.theovercaste.overdecompiler.attributes.*;
import user.theovercaste.overdecompiler.codeinternals.*;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.datahandlers.*;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.parserdata.ParsedField;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.JavaMethodParser;

import com.google.common.base.Optional;
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
            return ClassPath.getInternalPath(classData.getConstantPool().getClassName(classData.getParentId()));
        } catch (InvalidConstantPoolPointerException ex) {
            throw new ClassParsingException("Failed to get class parent.", ex);
        }
    }

    @Override
    public ClassPath getClassPath( ) throws ClassParsingException {
        try {
            return ClassPath.getInternalPath(classData.getConstantPool().getClassName(classData.getClassId()));
        } catch (InvalidConstantPoolPointerException ex) {
            throw new ClassParsingException("Failed to get classpath.", ex);
        }
    }

    @Override
    public void parseInterfaces( ) throws ClassParsingException {
        ConstantPool constantPool = classData.getConstantPool();
        try {
            for (int i : classData.getInterfaces()) {
                ClassPath classPath = ClassPath.getInternalPath(constantPool.getClassName(i));
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
                ParsedMethod parsedMethod = parseMethod(m);
                //for(AttributeData d : m.getAttributes()) {
                //    System.out.println("Method data: " + d.getName(classData.getConstantPool()));
                //}
                try { //Parse exceptions
                    Optional<ExceptionsAttribute> optionalExceptions = AttributeTypes.getWrappedAttribute(m.getAttributes(), classData.getConstantPool(), ExceptionsAttribute.class);
                    if(optionalExceptions.isPresent()) {
                         ArrayList<ClassPath> exceptions = new ArrayList<ClassPath>(optionalExceptions.get().getExceptions().length);
                        for(int i : optionalExceptions.get().getExceptions()) {
                            ClassPath exceptionPath = ClassPath.getInternalPath(classData.getConstantPool().getClassName(i));
                            exceptions.add(exceptionPath);
                        }
                        for(ClassPath exception : exceptions) {
                            parsedClass.addImport(exception);
                            parsedMethod.addException(exception);
                        }
                    }
                } catch (InvalidAttributeException e) {
                    e.printStackTrace(); //TODO proper logging, slf4j?
                }
                parsedClass.addMethod(parsedMethod);
            }
        } catch (InvalidConstantPoolPointerException ex) {
            throw new ClassParsingException("Failed to parse fields.", ex);
        }
    }

    @Override
    protected void parseAnnotations( ) throws ClassParsingException {
        // TODO
    }
    
    @Override
    protected void parseFlags( ) throws ClassParsingException {
        EnumSet<ClassFlag> classFlags = getClassFlags(classData.getFlagMask());
        if(classFlags.contains(ClassFlag.PUBLIC)) {
            parsedClass.addFlag(ClassFlag.PUBLIC);
        }
        if(classFlags.contains(ClassFlag.FINAL)) {
            parsedClass.addFlag(ClassFlag.FINAL);
        }
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
