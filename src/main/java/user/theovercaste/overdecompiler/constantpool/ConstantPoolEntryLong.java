package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryLong extends ConstantPoolEntry {
    protected final long value;

    public ConstantPoolEntryLong(long value) {
        super(ConstantPoolEntries.LONG_TAG);
        this.value = value;
    }

    public long getValue( ) {
        return value;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected long value;

        @Override
        public void read(DataInputStream din) throws IOException {
            value = din.readLong();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryLong(value);
        }
    }
}
