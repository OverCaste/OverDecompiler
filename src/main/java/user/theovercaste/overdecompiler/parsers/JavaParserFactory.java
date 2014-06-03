package user.theovercaste.overdecompiler.parsers;

import user.theovercaste.overdecompiler.parsers.AbstractParser.Factory;

public enum JavaParserFactory implements Factory {
    INSTANCE;

    @Override
    public AbstractParser createParser( ) {
        return new JavaParser();
    }
}
