package user.theovercaste.overdecompiler.parsers;

import java.util.ArrayList;
import java.util.Collection;

import user.theovercaste.overdecompiler.attributes.AttributableElement;
import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.attributes.RuntimeVisibleAnnotationsAttribute;
import user.theovercaste.overdecompiler.codeinternals.ClassFlag;
import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.ClassType;
import user.theovercaste.overdecompiler.codeinternals.FieldFlag;
import user.theovercaste.overdecompiler.codeinternals.MethodFlag;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryClass;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.ClassFlagHandler;
import user.theovercaste.overdecompiler.datahandlers.FieldData;
import user.theovercaste.overdecompiler.datahandlers.FieldFlagHandler;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.datahandlers.MethodFlagHandler;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedField;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.AbstractMethodParser;
import user.theovercaste.overdecompiler.parsers.methodparsers.JavaMethodParser;

public class JavaParser extends AbstractParser {
    private static final JavaMethodParser methodParser = new JavaMethodParser();

    private ParsedClass parsedClass;

    public ClassType getClassType(ClassData c) {
        ClassFlagHandler flagHandler = new ClassFlagHandler(c.getFlags());
        if (flagHandler.isEnum()) {
            return ClassType.ENUM;
        }
        if (flagHandler.isAnnotation()) {
            return ClassType.ANNOTATION;
        }
        if (flagHandler.isInterface()) {
            return ClassType.INTERFACE;
        }
        if (flagHandler.isAbstract()) {
            return ClassType.ABSTRACT_CLASS;
        }
        return ClassType.CLASS;
    }

    public Collection<ClassPath> getAnnotations(ClassData c, AttributableElement e) {
        ArrayList<ClassPath> ret = new ArrayList<ClassPath>(5); // You'll very rarely get > 5 annotations
        for (AttributeData d : e.getAttributes()) {
            try {
                if ("RuntimeVisibleAnnotations".equals(d.getName(c.getConstantPool()))) {
                    RuntimeVisibleAnnotationsAttribute attribute = RuntimeVisibleAnnotationsAttribute.parser().parse(d, c.getConstantPool());
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

    public ClassPath getClassParent(ClassData c) throws InvalidConstantPoolPointerException {
        PoolPreconditions.assertPoolRange(c.getParentId(), c.getConstantPool().length);
        ConstantPoolEntry poolEntry = c.getConstantPool()[c.getParentId()];
        if (poolEntry instanceof ConstantPoolEntryClass) {
            return new ClassPath(((ConstantPoolEntryClass) poolEntry).getName(c.getConstantPool()).replace("/", "."));
        } else {
            throw PoolPreconditions.getInvalidType(c.getConstantPool(), c.getParentId());
        }
    }

    public ClassPath getClassPath(ClassData c) throws InvalidConstantPoolPointerException {
        PoolPreconditions.assertPoolRange(c.getClassId(), c.getConstantPool().length);
        ConstantPoolEntry poolEntry = c.getConstantPool()[c.getClassId()];
        if (poolEntry instanceof ConstantPoolEntryClass) {
            return new ClassPath(((ConstantPoolEntryClass) poolEntry).getName(c.getConstantPool()).replace("/", "."));
        } else {
            throw PoolPreconditions.getInvalidType(c.getConstantPool(), c.getClassId());
        }
    }

    public void parseInterfaces(ClassData origin, ParsedClass value) throws InvalidConstantPoolPointerException {
        for (int i : origin.getInterfaces()) {
            PoolPreconditions.assertPoolRange(i, origin.getConstantPool().length);
            ConstantPoolEntry poolEntry = origin.getConstantPool()[i];
            if (poolEntry instanceof ConstantPoolEntryClass) {
                ClassPath classPath = new ClassPath(((ConstantPoolEntryClass) poolEntry).getName(origin.getConstantPool()).replace("/", "."));
                addImport(value, classPath);
                value.addInterface(classPath);
            } else {
                throw PoolPreconditions.getInvalidType(origin.getConstantPool(), origin.getParentId());
            }
        }
    }

    public void parseFields(ClassData origin, ParsedClass value) throws InvalidConstantPoolPointerException {
        for (FieldData f : origin.getFields()) {
            ClassPath classPath = ClassPath.getMangledPath(f.getDescription(origin.getConstantPool()));
            addImport(value, classPath);
            ParsedField parsed = new ParsedField(classPath, f.getName(origin.getConstantPool()));
            FieldFlagHandler flagHandler = f.getFlagHandler();
            if (flagHandler.isPublic()) {
                parsed.addFlag(FieldFlag.PUBLIC);
            } else if (flagHandler.isProtected()) {
                parsed.addFlag(FieldFlag.PROTECTED);
            } else if (flagHandler.isPrivate()) {
                parsed.addFlag(FieldFlag.PRIVATE);
            }
            if (flagHandler.isEnum()) {
                parsed.addFlag(FieldFlag.ENUM);
            }
            if (flagHandler.isFinal()) {
                parsed.addFlag(FieldFlag.FINAL);
            }
            if (flagHandler.isStatic()) {
                parsed.addFlag(FieldFlag.STATIC);
            }
            if (flagHandler.isVolatile()) {
                parsed.addFlag(FieldFlag.VOLATILE);
            }
            if (flagHandler.isTransient()) {
                parsed.addFlag(FieldFlag.TRANSIENT);
            }
            if (flagHandler.isSynthetic()) {
                parsed.addFlag(FieldFlag.SYNTHETIC);
            }
            value.addField(parsed);
        }
    }

    public void parseMethods(ClassData origin, ParsedClass value) throws InvalidConstantPoolPointerException {
        for (MethodData m : origin.getMethods()) {
            value.addMethod(parseMethod(origin, value, m));
        }
    }

    public ParsedMethod parseMethod(ClassData fromClass, ParsedClass toClass, MethodData data) throws InvalidConstantPoolPointerException {
        String descriptor = data.getDescription(fromClass.getConstantPool());
        ClassPath returnClassPath = ClassPath.getMethodReturnType(descriptor);
        addImport(toClass, returnClassPath);
        ParsedMethod parsed = new ParsedMethod(returnClassPath, data.getName(fromClass.getConstantPool()));
        for (ClassPath arg : ClassPath.getMethodArguments(descriptor)) {
            addImport(toClass, arg);
            parsed.addArgument(arg);
        }
        MethodFlagHandler flagHandler = data.getFlagHandler();
        if (flagHandler.isPublic()) {
            parsed.addFlag(MethodFlag.PUBLIC);
        } else if (flagHandler.isProtected()) {
            parsed.addFlag(MethodFlag.PROTECTED);
        } else if (flagHandler.isPrivate()) {
            parsed.addFlag(MethodFlag.PRIVATE);
        }
        if (flagHandler.isFinal()) {
            parsed.addFlag(MethodFlag.FINAL);
        }
        if (flagHandler.isStatic()) {
            parsed.addFlag(MethodFlag.STATIC);
        }
        if (flagHandler.isSynthetic()) {
            parsed.addFlag(MethodFlag.SYNTHETIC);
        }
        if (flagHandler.isSynchronized()) {
            parsed.addFlag(MethodFlag.SYNCHRONIZED);
        }
        if (flagHandler.isAbstract()) {
            parsed.addFlag(MethodFlag.ABSTRACT);
        }
        if (flagHandler.isBridge()) {
            parsed.addFlag(MethodFlag.BRIDGE);
        }
        if (flagHandler.isNative()) {
            parsed.addFlag(MethodFlag.NATIVE);
        }
        if (flagHandler.isStrict()) {
            parsed.addFlag(MethodFlag.STRICT);
        }
        if (flagHandler.isVarargs()) {
            parsed.addFlag(MethodFlag.VARARGS);
        }
        try {
            getMethodParser(fromClass).parseMethodActions(fromClass, toClass, data, parsed);
        } catch (InvalidAttributeException e) {
            e.printStackTrace();
        }
        return parsed;
    }

    protected void addImport(ParsedClass c, ClassPath i) {
        if (i.isObject() && !"java.lang".equals(i.getClassPackage())) {
            c.addImport(i);
        }
    }

    @Override
    public ParsedClass parseClass(ClassData c) throws InvalidConstantPoolPointerException {
        ClassPath parsedClassPath = getClassPath(c);
        parsedClass = new ParsedClass(parsedClassPath.getClassName(), parsedClassPath.getClassPackage(), getClassType(c), getClassParent(c));
        parseInterfaces(c, parsedClass);
        parseFields(c, parsedClass);
        parseMethods(c, parsedClass);
        parsedClass.addFlag(ClassFlag.PUBLIC);
        return parsedClass;
    }

    @Override
    public AbstractMethodParser getMethodParser(ClassData c) {
        return methodParser;
    }

    public static class Factory implements AbstractParserFactory {
        private static final Factory instance = new Factory();

        private Factory( ) {
            // do nothing
        }

        @Override
        public AbstractParser createParser( ) {
            return new JavaParser();
        }

        public static Factory getInstance( ) {
            return instance;
        }
    }
}
