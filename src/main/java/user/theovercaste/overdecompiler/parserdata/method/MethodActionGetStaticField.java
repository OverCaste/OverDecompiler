package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public class MethodActionGetStaticField extends MethodActionGetter {
	private String field;
	private ClassPath staticClass;

	public MethodActionGetStaticField(int lineNumber, String field, ClassPath staticClass) {
		super(lineNumber);
		this.field = field;
		this.staticClass = staticClass;
	}

	@Override
	public String getStringValue(ParsedClass c, ParsedMethod parent) {
		return staticClass.getClassName() + "." + field;
	}

	public String getField( ) {
		return field;
	}

	public ClassPath getStaticClass( ) {
		return staticClass;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setStaticClass(ClassPath staticClass) {
		this.staticClass = staticClass;
	}
}
