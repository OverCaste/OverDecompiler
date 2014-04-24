package user.theovercaste.overdecompiler.parserdata.method;

import java.util.Collection;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public abstract class MethodAction {
	private final int lineNumber;

	public MethodAction(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getLineNumber( ) {
		return lineNumber;
	}

	public boolean isContainer( ) {
		return false;
	}

	public Collection<MethodAction> getContainerContents( ) {
		throw new UnsupportedOperationException();
	}

	public abstract String getStringValue(ParsedClass c, ParsedMethod parent);
}
