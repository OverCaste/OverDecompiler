package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public class MethodActionAdd extends MethodActionGetter {
	private final MethodActionGetter valueOne;
	private final MethodActionGetter valueTwo;

	public MethodActionAdd(int lineNumber, MethodActionGetter valueOne, MethodActionGetter valueTwo) {
		super(lineNumber);
		this.valueOne = valueOne;
		this.valueTwo = valueTwo;
	}

	@Override
	public String getStringValue(ParsedClass c, ParsedMethod parent) {
		return valueOne.getStringValue(c, parent) + " + " + valueTwo.getStringValue(c, parent);
	}
}
