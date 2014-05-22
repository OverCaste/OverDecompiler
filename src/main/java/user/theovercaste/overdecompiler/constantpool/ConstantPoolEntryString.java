package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryString extends ConstantPoolEntry {
    protected final int stringIndex;

    public ConstantPoolEntryString(int tag, int stringIndex) {
        super(tag);
        this.stringIndex = stringIndex;
    }

    public int getStringIndex( ) {
        return stringIndex;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int stringIndex;

        @Override
        public void read(int tag, DataInputStream din) throws IOException {
            super.read(tag, din);
            stringIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryString(tag, stringIndex);
        }
    }
}
