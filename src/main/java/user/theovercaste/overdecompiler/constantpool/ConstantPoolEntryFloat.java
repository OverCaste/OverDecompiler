package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryFloat extends ConstantPoolEntry {
    protected final float value;

    public ConstantPoolEntryFloat(float value) {
        super(ConstantPoolEntries.FLOAT_TAG);
        this.value = value;
    }

    public float getValue( ) {
        return value;
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
