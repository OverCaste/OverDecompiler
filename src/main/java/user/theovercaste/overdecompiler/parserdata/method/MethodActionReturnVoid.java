package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public class MethodActionReturnVoid implements MethodAction {

	@Override
	public String getStringValue(ParsedClass c, ParsedMethod parent) {
		return "return";
	}
}
