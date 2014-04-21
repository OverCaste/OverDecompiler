package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public class MethodActionLoadVariable implements MethodActionGetter {
	private int variableIndex;

	public MethodActionLoadVariable(int variableIndex) {
		this.variableIndex = variableIndex;
	}

	@Override
	public String getStringValue(final ParsedClass c, final ParsedMethod parent) {
		return "v" + variableIndex; // TODO actual variable naming
	}
}
