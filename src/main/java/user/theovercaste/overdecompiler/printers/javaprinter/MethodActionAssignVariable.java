package user.theovercaste.overdecompiler.printers.javaprinter;

public class MethodActionAssignVariable {
	private final String variableName;
	private final MethodAction value;

	public MethodActionAssignVariable(String variableName, MethodAction value) {
		this.variableName = variableName;
		this.value = value;
	}

	public String getVariableName( ) {
		return variableName;
	}

	public MethodAction getValue( ) {
		return value;
	}
}
