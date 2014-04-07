package user.theovercaste.overdecompiler.parsers;

public class JavaParserFactory implements AbstractParserFactory {
	private static final JavaParserFactory instance = new JavaParserFactory();

	private JavaParserFactory( ) {
		// do nothing
	}

	@Override
	public AbstractParser createParser( ) {
		return new JavaParser();
	}

	public static JavaParserFactory getInstance( ) {
		return instance;
	}
}
