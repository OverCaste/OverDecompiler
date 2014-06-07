package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class ConstantPoolEntry {
    protected final int tag;

    public ConstantPoolEntry(int tag) {
        this.tag = tag;
    }

    public int getTag( ) {
        return tag;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        return true;
    }

    public abstract void write(DataOutputStream dout) throws IOException;

    public abstract static class Factory {
        public abstract void read(DataInputStream din) throws IOException;

        public abstract ConstantPoolEntry build( );
    }
}
