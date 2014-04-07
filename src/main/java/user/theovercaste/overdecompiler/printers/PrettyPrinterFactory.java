package user.theovercaste.overdecompiler.printers;

public class PrettyPrinterFactory implements AbstractPrinterFactory {
	private static final PrettyPrinterFactory instance = new PrettyPrinterFactory();

	private PrettyPrinterFactory( ) {
		// do nothing
	}

	@Override
	public AbstractPrinter createPrinter( ) {
		return new PrettyPrinter();
	}

	public static PrettyPrinterFactory getInstance( ) {
		return instance;
	}
}
