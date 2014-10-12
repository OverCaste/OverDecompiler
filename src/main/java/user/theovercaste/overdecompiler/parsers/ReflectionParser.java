package user.theovercaste.overdecompiler.parsers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.ClassType;
import user.theovercaste.overdecompiler.codeinternals.FieldFlag;
import user.theovercaste.overdecompiler.codeinternals.MethodFlag;
import user.theovercaste.overdecompiler.exceptions.ClassParsingException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedField;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

import com.google.common.base.Preconditions;

/**
 * This parser loads a class through reflection, however, it doesn't load method bodies, as those aren't available via reflection.
 * 
 * If you want to obtain method bodies, use {@link Class#getResource(String)} with a {@link JavaParser}.
 * 
 * @author <a href="http://www.reddit.com/user/TheOverCaste/">OverCaste</a>
 *
 */
public class ReflectionParser extends AbstractParser {
    protected final Class<?> originClass;
    protected ParsedClass parsedClass;

    public ReflectionParser(Class<?> originClass) {
        Preconditions.checkNotNull(originClass);
        Preconditions.checkArgument(originClass != Object.class, "Unable to parse the Object class!");
        Preconditions.checkArgument(!originClass.isArray(), "Unable to parse arrays!");
        Preconditions.checkArgument(!originClass.isPrimitive(), "Unable to parse primitives!");
        this.originClass = originClass;
    }

    protected ClassPath convertClassPath(Class<?> clazz) {
        return ClassPath.getMangledPath(clazz.getName());
    }

    @Override
    protected ClassPath getParentPath( ) {
        return convertClassPath(originClass.getSuperclass());
    }

    @Override
    protected ClassPath getClassPath( ) throws ClassParsingException {
        return convertClassPath(originClass);
    }

    @Override
    protected ClassType getClassType( ) {
        if (originClass.isInterface()) {
            return ClassType.INTERFACE;
        }
        if (originClass.isAnnotation()) {
            return ClassType.ANNOTATION;
        }
        if (originClass.isEnum()) {
            return ClassType.ENUM;
        }
        if (Modifier.isAbstract(originClass.getModifiers())) {
            return ClassType.ABSTRACT_CLASS;
        }
        return ClassType.CLASS;
    }

    protected void parseInterface(Class<?> i) {
        parsedClass.addInterface(convertClassPath(i));
    }

    @Override
    protected void parseInterfaces( ) throws ClassParsingException {
        for (Class<?> i : originClass.getInterfaces()) {
            parseInterface(i);
        }
    }

    protected void parseField(Field f) {
        ParsedField parsedField = new ParsedField(convertClassPath(f.getType()), f.getName());
        for (Annotation a : f.getDeclaredAnnotations()) {
            parsedField.addAnnotation(convertClassPath(a.annotationType()));
        }
        for (FieldFlag flag : getFieldFlags(f.getModifiers())) {
            parsedField.addFlag(flag);
        }
        parsedClass.addField(parsedField);
    }

    @Override
    protected void parseFields( ) throws ClassParsingException {
        for (Field f : originClass.getDeclaredFields()) {
            parseField(f);
        }
    }

    protected void parseMethod(Method m) {
        ParsedMethod parsedMethod = new ParsedMethod(convertClassPath(m.getReturnType()), m.getName());
        for (Annotation a : m.getDeclaredAnnotations()) {
            parsedMethod.addAnnotation(convertClassPath(a.annotationType()));
        }
        for (MethodFlag flag : getMethodFlags(m.getModifiers())) {
            parsedMethod.addFlag(flag);
        }
        for (Class<?> c : m.getParameterTypes()) {
            parsedMethod.addArgument(convertClassPath(c));
        }
        for (Class<?> exception : m.getExceptionTypes()) {
            parsedMethod.addException(convertClassPath(exception));
        }
        // TODO parameter annotations
        parsedClass.addMethod(parsedMethod);
    }

    @Override
    protected void parseMethods( ) throws ClassParsingException {
        for (Method m : originClass.getDeclaredMethods()) {
            parseMethod(m);
        }
    }

    @Override
    protected void parseAnnotations( ) throws ClassParsingException {
        for (Annotation a : originClass.getDeclaredAnnotations()) {
            parsedClass.addAnnotation(convertClassPath(a.annotationType()));
        }
    }
}
