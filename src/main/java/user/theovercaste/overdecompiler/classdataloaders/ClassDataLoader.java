package user.theovercaste.overdecompiler.classdataloaders;

import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;

public interface ClassDataLoader {
    public abstract ParsedClass getClassData( ) throws InvalidClassException, IOException;
}
