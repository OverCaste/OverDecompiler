package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

public class ConstantPoolEntryLong extends ConstantPoolEntry {
    protected final long value;

    public ConstantPoolEntryLong(long value) {
        super(ConstantPoolEntries.LONG_TAG);
        this.value = value;
    }

    public long getValue( ) {
        return value;
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(tag, value);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        return ((ConstantPoolEntryLong) other).value == value;
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
