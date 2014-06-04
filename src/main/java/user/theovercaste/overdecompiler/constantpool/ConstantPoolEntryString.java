package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryString extends ConstantPoolEntry {
    protected final int stringIndex;

    public ConstantPoolEntryString(int stringIndex) {
        super(ConstantPoolEntries.STRING_TAG);
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
        public void read(DataInputStream din) throws IOException {
            stringIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryString(stringIndex);
        }
    }
}
