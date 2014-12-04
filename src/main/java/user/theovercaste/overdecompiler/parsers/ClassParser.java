package user.theovercaste.overdecompiler.parsers;

import user.theovercaste.overdecompiler.exceptions.ClassParsingException;
import user.theovercaste.overdecompiler.parseddata.ParsedClass;

public interface ClassParser {
    public abstract ParsedClass parseClass( ) throws ClassParsingException;
}
