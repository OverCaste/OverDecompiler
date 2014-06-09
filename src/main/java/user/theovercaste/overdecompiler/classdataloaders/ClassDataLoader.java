package user.theovercaste.overdecompiler.classdataloaders;

import java.io.IOException;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;

public interface ClassDataLoader {
    public abstract ClassData getClassData( ) throws InvalidClassException, IOException;
}
