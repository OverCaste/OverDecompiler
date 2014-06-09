package user.theovercaste.overdecompiler.parsers;

import user.theovercaste.overdecompiler.exceptions.ClassParsingException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;

public interface ClassParser {
    public abstract ParsedClass parseClass( ) throws ClassParsingException;
}
