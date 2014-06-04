package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryInteger extends ConstantPoolEntry {
    protected final int value;

    public ConstantPoolEntryInteger(int value) {
        super(ConstantPoolEntries.INTEGER_TAG);
        this.value = value;
    }

    public int getValue( ) {
        return value;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int value;

        @Override
        public void read(DataInputStream din) throws IOException {
            value = din.readInt();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryInteger(value);
        }
    }
}
