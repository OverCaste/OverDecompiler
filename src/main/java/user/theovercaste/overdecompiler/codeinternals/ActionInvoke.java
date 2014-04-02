package user.theovercaste.overdecompiler.codeinternals;

public class ActionInvoke implements MethodValue {
	private final ClassPath invokeeClass; // ex java.lang.System
	private final String invokeeObject; // ex out
	private final String method;// ex println
	private final MethodValue object; // ex aField or "a" or 5

	public ActionInvoke(ClassPath invokeeClass, String invokeeObject, String method, MethodValue object) {
		this.invokeeClass = invokeeClass;
		this.invokeeObject = invokeeObject;
		this.method = method;
		this.object = object;
	}

	public ClassPath getInvokeeClass( ) {
		return invokeeClass;
	}

	public String getInvokeeObject( ) {
		return invokeeObject;
	}

	public String getMethod( ) {
		return method;
	}

	public MethodValue getObject( ) {
		return object;
	}

	@Override
	public String getPlainVersion( ) {
		return null;
	}
}
