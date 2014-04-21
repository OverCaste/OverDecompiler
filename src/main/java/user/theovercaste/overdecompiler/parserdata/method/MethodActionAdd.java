package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public class MethodActionAdd implements MethodActionGetter {
	private final MethodActionGetter valueOne;
	private final MethodActionGetter valueTwo;

	public MethodActionAdd(MethodActionGetter valueOne, MethodActionGetter valueTwo) {
		this.valueOne = valueOne;
		this.valueTwo = valueTwo;
	}

	@Override
	public String getStringValue(ParsedClass c, ParsedMethod parent) {
		return valueOne.getStringValue(c, parent) + " + " + valueTwo.getStringValue(c, parent);
	}
}
