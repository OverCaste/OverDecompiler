package user.theovercaste.overdecompiler.codeinternals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;

public class ClassPath {
    private static final BiMap<Character, ClassPath> primitiveIdentifiers = HashBiMap.create();
    private static final BiMap<Character, String> primitiveNames = HashBiMap.create();

    /** Equivalent to the 'void' keyword. */
    public static final ClassPath VOID = createPrimitivePath("void", 'V');
    /** Equivalent to the 'boolean' keyword. */
    public static final ClassPath BOOLEAN = createPrimitivePath("boolean", 'Z');
    public static final ClassPath BYTE = createPrimitivePath("byte", 'B');
    public static final ClassPath CHAR = createPrimitivePath("char", 'C');
    public static final ClassPath SHORT = createPrimitivePath("short", 'S');
    public static final ClassPath INTEGER = createPrimitivePath("int", 'I');
    public static final ClassPath LONG = createPrimitivePath("long", 'J');
    public static final ClassPath FLOAT = createPrimitivePath("float", 'F');
    public static final ClassPath DOUBLE = createPrimitivePath("double", 'D');

    public static final ClassPath OBJECT = new ClassPath("Object", "java.lang", 0, true);
    public static final ClassPath OBJECT_ANNOTATION = new ClassPath("Annotation", "java.lang.annotation", 0, true);
    public static final ClassPath OBJECT_ENUM = new ClassPath("Enum", "java.lang", 0, true);
    public static final ClassPath OBJECT_LONG = new ClassPath("Long", "java.lang", 0, true);
    public static final ClassPath OBJECT_INTEGER = new ClassPath("Integer", "java.lang", 0, true);
    public static final ClassPath OBJECT_SHORT = new ClassPath("Short", "java.lang", 0, true);
    public static final ClassPath OBJECT_BYTE = new ClassPath("Byte", "java.lang", 0, true);
    public static final ClassPath OBJECT_CHARACTER = new ClassPath("Character", "java.lang", 0, true);
    public static final ClassPath OBJECT_FLOAT = new ClassPath("Float", "java.lang", 0, true);
    public static final ClassPath OBJECT_DOUBLE = new ClassPath("Double", "java.lang", 0, true);
    public static final ClassPath OBJECT_STRING = new ClassPath("String", "java.lang", 0, true);

    private static ClassPath createPrimitivePath(String name, char identifier) {
        ClassPath ret = new ClassPath(name, "", 0, false);
        primitiveIdentifiers.put(identifier, ret);
        primitiveNames.put(identifier, name);
        return ret;
    }

    private final String className;
    private final String classPackage;
    private final int arrayDepth;
    private final boolean isObject;

    protected ClassPath(String className, String classPackage, int arrayDepth, boolean isObject) {
        this.className = className;
        this.classPackage = classPackage;
        this.arrayDepth = arrayDepth;
        this.isObject = isObject;
    }

    /**
     * Creates a ClassPath from a base string, such as [Ljava/lang/String (String[])
     * 
     * @param qualifiedName
     * @see<a href=http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.3>The definition of bytecode field descriptors at the oracle site</a>.
     */
    public ClassPath(String qualifiedName, int arrayDepth) {
        Preconditions.checkNotNull(qualifiedName, "Qualified name is null!");
        if (qualifiedName.indexOf("/") >= 0) { // Remove slashes
            qualifiedName = qualifiedName.replace("/", ".");
        }
        int lastPeriodIndex = qualifiedName.lastIndexOf(".");
        if (lastPeriodIndex != -1) {
            className = qualifiedName.substring(lastPeriodIndex + 1, qualifiedName.length());
            classPackage = qualifiedName.substring(0, lastPeriodIndex);
        } else {
            className = qualifiedName;
            classPackage = "";
        }
        this.arrayDepth = arrayDepth;
        isObject = true;
    }

    public ClassPath(String qualifiedName) {
        this(qualifiedName, 0);
    }

    /**
     * @return A simple representation of what this class is. Example: java.lang.Object
     */
    public String getSimplePath( ) {
        StringBuilder ret = new StringBuilder();
        if (classPackage.length() > 0) {
            ret.append(classPackage).append(".");
        }
        ret.append(className);
        return ret.toString();
    }

    /**
     * @return A representation of how you would define this ClassPath in code. Example: Object[][]
     */
    public String getDefinition( ) {
        StringBuilder ret = new StringBuilder(className);
        for (int i = 0; i < arrayDepth; i++) {
            ret.append("[]");
        }
        return ret.toString();
    }

    /**
     * @return A representation of how you would fully define this ClassPath in code. Example: java.lang.Object[][]
     */
    public String getFullDefinition( ) {
        StringBuilder ret = new StringBuilder(getSimplePath());
        for (int i = 0; i < arrayDepth; i++) {
            ret.append("[]");
        }
        return ret.toString();
    }

    public String getClassName( ) {
        return className;
    }

    public String getClassPackage( ) {
        return classPackage;
    }

    public int getArrayDepth( ) {
        return arrayDepth;
    }

    /**
     * Checks whether {@link #getArrayDepth()} returns one or more.
     * 
     * @return True if this classpath represents an array, false otherwise
     */
    public boolean isArray( ) {
        return arrayDepth > 0;
    }

    public boolean isObject( ) {
        return isObject;
    }

    public ClassPath asArray(int depth) {
        if (depth == 0) {
            return this;
        }
        return new ClassPath(className, classPackage, depth, isObject);
    }

    /**
     * Creates a mangled string from this classpath, for example [Ljava/lang/String
     * 
     * @return The mangled version of this path
     */
    public String toMangled( ) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < arrayDepth; i++) {
            ret.append('[');
        }
        if (isObject) {
            ret.append('L');
            ret.append(getSimplePath().replace('.', '/'));
        } else {
            if (primitiveNames.inverse().containsKey(className)) {
                ret.append(primitiveNames.inverse().get(className));
            } else {
                throw new RuntimeException("Primitive names didn't contain this primitive's name? (" + className + ")");
            }
        }
        return ret.toString();
    }

    public static String toMethodDefinition(ClassPath returnType, Collection<ClassPath> arguments) {
        StringBuilder ret = new StringBuilder();
        ret.append('(');
        ret.append(Joiner.on(";").join(Iterables.transform(arguments, new Function<ClassPath, String>() {
            @Override
            public String apply(ClassPath input) {
                return input.toMangled();
            }
        }))); // Convert arguments into a semicolon separated mangled path list.
        ret.append(")");
        ret.append(returnType.toMangled());
        return ret.toString();
    }

    /**
     * Creates a classpath from a mangled path, for example [Ljava/lang/String
     * 
     * @param mangledPath
     * @return
     */
    public static ClassPath getMangledPath(String mangledPath) {
        return Iterables.getOnlyElement(getMangledPaths(mangledPath));
    }

    public static Collection<ClassPath> getMangledPaths(String input) {
        if (input.length() == 0) {
            return new ArrayList<ClassPath>(0);
        }
        Splitter splitter = Splitter.on(";").omitEmptyStrings();
        Iterable<String> split = splitter.split(input);
        ArrayList<ClassPath> ret = new ArrayList<ClassPath>();
        for (String s : split) {
            ret.add(demangle(s));
        }
        return ret;
    }

    /**
     * Retrieves the classpaths from a method's descriptor, for example ([Ljava/lang/String;)V would take a single depth array of java.lang.String paths.
     * 
     * @param descriptor The method's internal descriptor.
     * @return The argument classpaths.
     */
    public static Collection<ClassPath> getMethodArguments(String descriptor) {
        int closingBraceIndex = descriptor.indexOf(")");
        String parameterDescriptor = (closingBraceIndex < 0 ? "" : descriptor.substring(1, closingBraceIndex));
        return getMangledPaths(parameterDescriptor);
    }

    /**
     * Retrieves the classpath from a method's return type, for example ([Ljava/lang/String;)V would return {@link #VOID}
     * 
     * @param descriptor The method's internal descriptor.
     * @return The return classpath.
     */
    public static ClassPath getMethodReturnType(String descriptor) {
        int closingBraceIndex = descriptor.indexOf(")");
        String returnDescriptor = (closingBraceIndex < 0 ? descriptor : descriptor.substring(closingBraceIndex + 1, descriptor.length()));
        return demangle(returnDescriptor);
    }

    /**
     * An internal method to generate a classpath from a path without any separators.
     * 
     * @param mangled The mangled path to retrieve the classpath from.
     * @return
     */
    private static ClassPath demangle(String mangled) {
        if (mangled.length() == 0) {
            return VOID;
        }
        int arrayDepth = 0;
        for (int i = 0; mangled.charAt(i) == '['; i++) {
            arrayDepth++;
        }
        if (arrayDepth > 0) {
            mangled = mangled.substring(arrayDepth);
        }
        char descriptor = mangled.charAt(0);
        if (descriptor == 'L') { // Object
            return new ClassPath(mangled.substring(1), arrayDepth);
        }
        // Otherwise it's a primitive
        if (primitiveIdentifiers.containsKey(descriptor)) {
            return primitiveIdentifiers.get(descriptor).asArray(arrayDepth);
        }
        throw new IllegalArgumentException("The path \"" + mangled + "\" isn't a valid, mangled path!");
    }

    @Override
    public String toString( ) {
        return "ClassPath [" + getFullDefinition() + "]";
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(arrayDepth, className, classPackage, isObject);
    }

    public boolean equalsType(ClassPath other) {
        return Objects.equals(other.className, className) && Objects.equals(other.classPackage, classPackage) && (other.isObject == isObject);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ClassPath other = (ClassPath) obj;
        if (arrayDepth != other.arrayDepth) {
            return false;
        }
        if (!Objects.equals(className, other.className)) {
            return false;
        }
        if (!Objects.equals(classPackage, other.classPackage)) {
            return false;
        }
        if (isObject != other.isObject) {
            return false;
        }
        return true;
    }

}
