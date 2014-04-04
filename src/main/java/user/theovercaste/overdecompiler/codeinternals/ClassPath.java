package user.theovercaste.overdecompiler.codeinternals;

public class ClassPath {
	public static final ClassPath VOID = new ClassPath("void", "", 0, false);
	public static final ClassPath BOOLEAN = new ClassPath("boolean", "", 0, false);
	public static final ClassPath BYTE = new ClassPath("byte", "", 0, false);
	public static final ClassPath CHAR = new ClassPath("char", "", 0, false);
	public static final ClassPath SHORT = new ClassPath("short", "", 0, false);
	public static final ClassPath INTEGER = new ClassPath("int", "", 0, false);
	public static final ClassPath LONG = new ClassPath("long", "", 0, false);
	public static final ClassPath FLOAT = new ClassPath("float", "", 0, false);
	public static final ClassPath DOUBLE = new ClassPath("double", "", 0, false);
	public static final ClassPath OBJECT = new ClassPath("Object", "java.lang", 0, true);

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
	 * Returns a simple representation of what this class is. Example: java.lang.Object[]
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

	public boolean isObject( ) {
		return true;
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
