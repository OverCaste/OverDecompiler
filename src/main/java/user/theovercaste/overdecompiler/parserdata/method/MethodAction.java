package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public interface MethodAction {
	public abstract String getStringValue(ParsedClass c, ParsedMethod parent);
}
