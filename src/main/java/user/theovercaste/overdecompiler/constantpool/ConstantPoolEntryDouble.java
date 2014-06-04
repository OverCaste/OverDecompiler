package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

public class ConstantPoolEntryDouble extends ConstantPoolEntry {
    protected final double value;

    public ConstantPoolEntryDouble(double value) {
        super(ConstantPoolEntries.DOUBLE_TAG);
        this.value = value;
    }

    public double getValue( ) {
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
        return ((ConstantPoolEntryDouble) other).value == value;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected double value;

        @Override
        public void read(DataInputStream din) throws IOException {
            value = din.readDouble();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryDouble(value);
        }
    }
}
