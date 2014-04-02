package user.theovercaste.overdecompiler.codeinternals;

public class ClassPath {
	private final String className;
	private final String classPackage;
	private final int arrayDepth;

	public ClassPath(String className, String classPackage, int arrayDepth) {
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
	public ClassPath(String qualifiedName) {
		if (qualifiedName == null) {
			throw new NullPointerException("Qualified name is null!");
		}
		int arrayDepth = 0;
		for (int i = 0; qualifiedName.charAt(i) == '['; i++) {
			arrayDepth++;
		}
		if (arrayDepth > 0) {
			qualifiedName = qualifiedName.substring(1 + arrayDepth);
		}
		qualifiedName = qualifiedName.replace('/', '.');
		int lastPeriodIndex = qualifiedName.lastIndexOf(".");
		if (lastPeriodIndex == -1) {
			throw new IllegalArgumentException("Qualified name isn't a valid, slash separated path.");
		}
		className = qualifiedName.substring(lastPeriodIndex, qualifiedName.length());
		classPackage = qualifiedName.substring(0, lastPeriodIndex - 1);
		this.arrayDepth = arrayDepth;
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
}
