package user.theovercaste.overdecompiler.classdataloaders;

import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;

public interface ClassDataLoader {
    public abstract ClassData getClassData( ) throws InvalidClassException;
}
