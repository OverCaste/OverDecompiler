package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class ConstantPoolEntry {
    protected final int tag;

    public ConstantPoolEntry(int tag) {
        this.tag = tag;
    }

    public int getTag( ) {
        return tag;
    }

    public abstract static class Factory {
        public abstract void read(DataInputStream din) throws IOException;

        public abstract ConstantPoolEntry build( );
    }
}
