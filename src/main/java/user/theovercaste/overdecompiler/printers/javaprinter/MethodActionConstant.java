package user.theovercaste.overdecompiler.printers.javaprinter;

public class MethodActionConstant extends MethodAction {
	private final String value;

	public MethodActionConstant(String value) {
		this.value = value;
	}

	public String getValue( ) {
		return value;
	}

	@Override
	public String getStringValue( ) {
		return value;
	}
}
