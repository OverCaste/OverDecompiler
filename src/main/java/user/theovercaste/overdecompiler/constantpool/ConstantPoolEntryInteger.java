package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryInteger extends ConstantPoolEntry {
    protected final int value;

    public ConstantPoolEntryInteger(int tag, int value) {
        super(tag);
        this.value = value;
    }

    public int getValue( ) {
        return this.value;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int value;

        @Override
        public void read(int tag, DataInputStream din) throws IOException {
            super.read(tag, din);
            this.value = din.readInt();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryInteger(this.tag, this.value);
        }
    }
}
