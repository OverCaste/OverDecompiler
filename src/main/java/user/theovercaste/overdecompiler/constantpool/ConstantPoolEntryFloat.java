package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ConstantPoolEntryFloat extends ConstantPoolEntry {
    protected final float value;

    public ConstantPoolEntryFloat(float value) {
        super(ConstantPoolEntries.FLOAT_TAG);
        this.value = value;
    }

    public float getValue( ) {
        return value;
    }

    @Override
    public void write(DataOutputStream dout) throws IOException {
        dout.writeFloat(value);
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
        return ((ConstantPoolEntryFloat) other).value == value;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected float value;

        @Override
        public void read(DataInputStream din) throws IOException {
            value = din.readFloat();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryFloat(value);
        }
    }
}
