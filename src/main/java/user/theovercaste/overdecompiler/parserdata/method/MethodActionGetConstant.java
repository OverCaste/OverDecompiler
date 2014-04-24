package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

import com.google.common.base.Preconditions;

public class MethodActionGetConstant extends MethodActionGetter {
	public enum ConstantType {
		CHAR,
		BYTE,
		SHORT,
		INT,
		LONG,
		FLOAT,
		DOUBLE,
		STRING,
		NULL;
	}

	private String value;
	private ConstantType type;

	public MethodActionGetConstant(int lineNumber, String value, ConstantType type) {
		super(lineNumber);
		Preconditions.checkNotNull(type);
		if (type == ConstantType.NULL) {
			Preconditions.checkArgument(value == null, "If type is 'null', value must be null!");
		}
		this.value = value;
		this.type = type;
	}

	@Override
	public String getStringValue(ParsedClass c, ParsedMethod parent) {
		switch (type) {
			case CHAR:
				return "'" + value + "'";
			case BYTE:
				return "(byte)" + value;
			case SHORT:
				return value + "s";
			case INT:
				return value;
			case LONG:
			case DOUBLE:
				return value + "l";
			case FLOAT:
				return value + "f";
			case STRING:
				return "\"" + value + "\"";
			case NULL:
				return "null";
		}
		return "<ERROR INVALID TYPE: " + type.name() + ">"; // Try to decompile even if we fail.
	}

	public String getValue( ) {
		return value;
	}

	public ConstantType getType( ) {
		return type;
	}
}
