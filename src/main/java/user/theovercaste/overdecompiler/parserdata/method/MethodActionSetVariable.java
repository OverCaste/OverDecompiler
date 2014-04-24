package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public class MethodActionSetVariable extends MethodActionGetter {
	private final int variableIndex;
	private final MethodActionGetter expression;

	public MethodActionSetVariable(int lineNumber, int variableIndex, MethodActionGetter expression) {
		super(lineNumber);
		this.variableIndex = variableIndex;
		this.expression = expression;
	}

	@Override
	public String getStringValue(ParsedClass c, ParsedMethod parent) {
		return "v" + variableIndex + " = " + expression.getStringValue(c, parent); // TODO actual variable naming
	}

	public int getVariable( ) {
		return variableIndex;
	}

	public MethodAction getExpression( ) {
		return expression;
	}
}
