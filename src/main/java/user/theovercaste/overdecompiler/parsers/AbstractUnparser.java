package user.theovercaste.overdecompiler.parsers;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;

public abstract class AbstractUnparser {
    public abstract ClassData unparseClass(ParsedClass c);

    public abstract interface Factory {
        public abstract AbstractUnparser createUnparser( );
    }
}
