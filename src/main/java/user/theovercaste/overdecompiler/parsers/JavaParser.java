package user.theovercaste.overdecompiler.parsers;

import java.util.ArrayList;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.theovercaste.overdecompiler.attributes.*;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.parseddata.ParsedField;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parseddata.annotation.ParsedAnnotation;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.JavaAnnotationParser;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.JavaImportParser;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.JavaMethodParser;
import user.theovercaste.overdecompiler.rawclassdata.*;
import user.theovercaste.overdecompiler.util.*;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class JavaParser extends AbstractParser {
    private static final Logger logger = LoggerFactory.getLogger(JavaParser.class);
    protected static final JavaMethodParser methodParser = new JavaMethodParser();
    protected static final JavaImportParser importParser = new JavaImportParser();
    protected static final JavaAnnotationParser annotationParser = new JavaAnnotationParser();

    protected final ClassData classData;

    public JavaParser(ClassData classData) {
        Preconditions.checkNotNull(classData);
        this.classData = classData;
    }

    public ImmutableList<ParsedAnnotation> getAnnotations(AttributableElement e) throws InvalidAttributeException {
        Optional<RuntimeVisibleAnnotationsAttribute> optionalVisibleAnnotations = Attributes.loadAttribute(e.getAttributes(), classData.getConstantPool(), RuntimeVisibleAnnotationsAttribute.class);
        if (optionalVisibleAnnotations.isPresent()) {
            ImmutableList.Builder<ParsedAnnotation> annotationBuilder = ImmutableList.builder();
            for (RuntimeVisibleAnnotationsAttribute.Annotation a : optionalVisibleAnnotations.get().getAnnotations()) {
                annotationBuilder.add(annotationParser.parseAnnotation(a));
            }
            return annotationBuilder.build();
        }
        return ImmutableList.of();
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

    @Override
    protected JavaVersion getJavaVersion( ) throws ClassParsingException {
        if (JavaVersion.checkInternalVersionExists(classData.getMajorVersion())) {
            return JavaVersion.getByInternalVersion(classData.getMajorVersion());
        }
        return JavaVersion.UNKNOWN;
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
                try { // Parse exceptions
                    Optional<ExceptionsAttribute> optionalExceptions = Attributes.loadAttribute(m.getAttributes(), classData.getConstantPool(), ExceptionsAttribute.class);
                    if (optionalExceptions.isPresent()) {
                        ArrayList<ClassPath> exceptions = new ArrayList<ClassPath>(optionalExceptions.get().getExceptions().length);
                        for (int i : optionalExceptions.get().getExceptions()) {
                            ClassPath exceptionPath = ClassPath.getInternalPath(classData.getConstantPool().getClassName(i));
                            exceptions.add(exceptionPath);
                        }
                        for (ClassPath exception : exceptions) {
                            parsedMethod.addException(exception);
                        }
                    }
                } catch (InvalidAttributeException e) {
                    logger.warn("Failed to read the exceptions attribute for method {} in class {}.", parsedMethod.getName(), parsedClass.getName(), e);
                }
                try {
                    for (ParsedAnnotation a : getAnnotations(m)) {
                        parsedMethod.addAnnotation(a);
                    }
                } catch (InvalidAttributeException ex) {
                    logger.warn("Failed to read the annotations attribute for method {} in class {}.", parsedMethod.getName(), parsedClass.getName(), ex);
                }
                if (parsedMethod.getName().equals("<init>")) {
                    parsedClass.addConstructor(parsedMethod);
                } else {
                    parsedClass.addMethod(parsedMethod);
                }
            }
        } catch (InvalidConstantPoolPointerException ex) {
            throw new ClassParsingException("Failed to parse fields.", ex);
        }
    }

    @Override
    protected void parseAnnotations( ) throws ClassParsingException {
        try {
            for (ParsedAnnotation annotation : getAnnotations(classData)) {
                parsedClass.addAnnotation(annotation);
            }
        } catch (InvalidAttributeException ex) {
            logger.warn("Failed to read the annotations attribute for class {}", parsedClass.getName(), ex);
        }
    }

    @Override
    protected void parseFlags( ) throws ClassParsingException {
        EnumSet<ClassFlag> classFlags = getClassFlags(classData.getFlagMask());
        if (classFlags.contains(ClassFlag.PUBLIC)) {
            parsedClass.addFlag(ClassFlag.PUBLIC);
        }
        if (classFlags.contains(ClassFlag.FINAL)) {
            parsedClass.addFlag(ClassFlag.FINAL);
        }
    }

    @Override
    protected void parseImports( ) throws ClassParsingException {
        importParser.parseImports(parsedClass);
    }

    public ParsedMethod parseMethod(MethodData m) throws InvalidConstantPoolPointerException {
        String descriptor = m.getDescription(classData.getConstantPool());
        ClassPath returnClassPath = ClassPath.getMethodReturnType(descriptor);
        ParsedMethod parsed = new ParsedMethod(returnClassPath, m.getName(classData.getConstantPool()));
        for (ClassPath arg : ClassPath.getMethodArguments(descriptor)) {
            parsed.addArgument(arg);
        }
        for (MethodFlag flag : getMethodFlags(m.getFlagMask())) {
            parsed.addFlag(flag); // TODO eventually change this to .addAll or something
        }
        try {
            methodParser.parseMethodActions(classData, parsedClass, m, parsed);
        } catch (InvalidAttributeException e) { // Catch all of these so that decompilation may continue even if bad things happen.
            logger.warn("An invalid attribute exception was thrown while parsing method {} in class {}.", parsed.getName(), parsedClass.getName(), e);
        } catch (IllegalArgumentException e) {
            logger.warn("An illegal argument exception was thrown while parsing method {} in class {}.", parsed.getName(), parsedClass.getName(), e);
        } catch (NullPointerException e) {
            logger.warn("A null pointer exception was thrown while parsing method {} in class {}.", parsed.getName(), parsedClass.getName(), e);
        } catch (IndexOutOfBoundsException e) {
            logger.warn("An Index out of bounds exception was thrown while parsing {} in class {}.", parsed.getName(), parsedClass.getName(), e);
        }
        return parsed;
    }
}
