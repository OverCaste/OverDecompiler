package user.theovercaste.overdecompiler.codeinternals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import com.google.common.collect.Iterables;

public class ClassPath {
	/** Equivalent to the 'void' keyword. */
	public static final ClassPath VOID = new ClassPath("void", "", 0, false);
	/** Equivalent to the 'boolean' keyword. */
	public static final ClassPath BOOLEAN = new ClassPath("boolean", "", 0, false);
	public static final ClassPath BYTE = new ClassPath("byte", "", 0, false);
	public static final ClassPath CHAR = new ClassPath("char", "", 0, false);
	public static final ClassPath SHORT = new ClassPath("short", "", 0, false);
	public static final ClassPath INTEGER = new ClassPath("int", "", 0, false);
	public static final ClassPath LONG = new ClassPath("long", "", 0, false);
	public static final ClassPath FLOAT = new ClassPath("float", "", 0, false);
	public static final ClassPath DOUBLE = new ClassPath("double", "", 0, false);

	public static final ClassPath OBJECT = new ClassPath("Object", "java.lang", 0, true);
	public static final ClassPath OBJECT_ENUM = new ClassPath("Enum", "java.lang", 0, true);
	public static final ClassPath OBJECT_LONG = new ClassPath("Long", "java.lang", 0, true);
	public static final ClassPath OBJECT_INTEGER = new ClassPath("Integer", "java.lang", 0, true);
	public static final ClassPath OBJECT_SHORT = new ClassPath("Short", "java.lang", 0, true);
	public static final ClassPath OBJECT_BYTE = new ClassPath("Byte", "java.lang", 0, true);
	public static final ClassPath OBJECT_CHARACTER = new ClassPath("Character", "java.lang", 0, true);
	public static final ClassPath OBJECT_FLOAT = new ClassPath("Float", "java.lang", 0, true);
	public static final ClassPath OBJECT_DOUBLE = new ClassPath("Double", "java.lang", 0, true);
	public static final ClassPath OBJECT_STRING = new ClassPath("String", "java.lang", 0, true);

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
		if (qualifiedName == null) {
			throw new NullPointerException("Qualified name is null!");
		}
		if (qualifiedName.indexOf("/") > 0) { // Remove slashes
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
	 * Returns a simple representation of what this class is. Example: java.lang.Object
	 * 
	 * @return
	 */
	public String getSimplePath( ) {
		StringBuilder ret = new StringBuilder();
		if (classPackage.length() > 0) {
			ret.append(classPackage).append(".");
		}
		ret.append(className);
		return ret.toString();
	}

	public String getDefinition( ) {
		StringBuilder ret = new StringBuilder(className);
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
	 * @return True if this classpath represents an array, false otherwise.
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
		String[] split = input.split(";");
		ArrayList<ClassPath> ret = new ArrayList<ClassPath>(split.length);
		for (String s : split) {
			ret.add(demangle(s));
		}
		return ret;
	}

	/**
	 * Retrieves the classpaths from a method's descriptor, for example ([Ljava/lang/String;)V would return a single depth array of java.lang.String paths.
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
		switch (descriptor) {
			case 'Z':
				return BOOLEAN.asArray(arrayDepth);
			case 'B':
				return BYTE.asArray(arrayDepth);
			case 'S':
				return SHORT.asArray(arrayDepth);
			case 'I':
				return INTEGER.asArray(arrayDepth);
			case 'J':
				return LONG.asArray(arrayDepth);
			case 'F':
				return FLOAT.asArray(arrayDepth);
			case 'D':
				return DOUBLE.asArray(arrayDepth);
			case 'C':
				return CHAR.asArray(arrayDepth);
			case 'V':
				return VOID.asArray(arrayDepth);
		}
		throw new IllegalArgumentException("The path \"" + mangled + "\" isn't a valid, mangled path!");
	}

	@Override
	public int hashCode( ) {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + arrayDepth;
		result = (prime * result) + ((className == null) ? 0 : className.hashCode());
		result = (prime * result) + ((classPackage == null) ? 0 : classPackage.hashCode());
		result = (prime * result) + (isObject ? 1231 : 1237);
		return result;
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
		if (className == null) {
			if (other.className != null) {
				return false;
			}
		} else if (!className.equals(other.className)) {
			return false;
		}
		if (classPackage == null) {
			if (other.classPackage != null) {
				return false;
			}
		} else if (!classPackage.equals(other.classPackage)) {
			return false;
		}
		if (isObject != other.isObject) {
			return false;
		}
		return true;
	}
}
