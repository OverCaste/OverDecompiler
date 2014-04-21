package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public class MethodActionReturnValue implements MethodAction {
	protected final MethodActionGetter value;

	public MethodActionReturnValue(MethodActionGetter value) {
		this.value = value;
	}

	@Override
	public String getStringValue(ParsedClass c, ParsedMethod parent) {
		return "return " + value.getStringValue(c, parent);
	}
}
