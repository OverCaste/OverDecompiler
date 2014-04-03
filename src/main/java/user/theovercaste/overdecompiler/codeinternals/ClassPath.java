package user.theovercaste.overdecompiler.codeinternals;

public class ClassPath {
	public static final ClassPath VOID = new ClassPath("void", 0);
	public static final ClassPath BOOLEAN = new ClassPath("boolean", 0);
	public static final ClassPath BYTE = new ClassPath("byte", 0);
	public static final ClassPath CHAR = new ClassPath("char", 0);
	public static final ClassPath SHORT = new ClassPath("short", 0);
	public static final ClassPath INTEGER = new ClassPath("int", 0);
	public static final ClassPath LONG = new ClassPath("long", 0);
	public static final ClassPath FLOAT = new ClassPath("float", 0);
	public static final ClassPath DOUBLE = new ClassPath("double", 0);

	private final String className;
	private final String classPackage;
	private final int arrayDepth;

	protected ClassPath(String className, String classPackage, int arrayDepth) {
		this.className = className;
		this.classPackage = classPackage;
		this.arrayDepth = arrayDepth;
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
		int lastPeriodIndex = qualifiedName.lastIndexOf(".");
		if (lastPeriodIndex != -1) {
			className = qualifiedName.substring(lastPeriodIndex + 1, qualifiedName.length());
			classPackage = qualifiedName.substring(0, lastPeriodIndex);
		} else {
			className = qualifiedName;
			classPackage = "";
		}
		this.arrayDepth = arrayDepth;
	}

	public ClassPath(String qualifiedName) {
		this(qualifiedName, 0);
	}

	/**
	 * Returns a simple representation of what this class is. Example: java.lang.Object[]
	 * 
	 * @return
	 */
	public String getSimplePath( ) {
		StringBuilder ret = new StringBuilder(classPackage);
		ret.append(".").append(className);
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

	public ClassPath asArray(int depth) {
		if (depth == 0) {
			return this;
		}
		return new ClassPath(className, classPackage, depth);
	}

	/**
	 * Creates a classpath from a mangled path, for example [Ljava/lang/String
	 * 
	 * @param mangledPath
	 * @return
	 */
	public static ClassPath getMangledPath(String mangledPath) {
		int arrayDepth = 0;
		for (int i = 0; mangledPath.charAt(i) == '['; i++) {
			arrayDepth++;
		}
		if (arrayDepth > 0) {
			mangledPath = mangledPath.substring(arrayDepth);
		}
		char descriptor = mangledPath.charAt(0);
		if (descriptor == 'L') { // Object
			return new ClassPath(mangledPath.substring(1).replace("/", "."), arrayDepth);
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
		throw new IllegalArgumentException("That path isn't a valid, mangled path!");
	}
}
