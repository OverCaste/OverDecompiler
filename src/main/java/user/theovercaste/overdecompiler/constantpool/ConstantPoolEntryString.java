package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

public class ConstantPoolEntryString extends ConstantPoolEntry {
    protected final int stringIndex;

    public ConstantPoolEntryString(int stringIndex) {
        super(ConstantPoolEntries.STRING_TAG);
        this.stringIndex = stringIndex;
    }

    public int getStringIndex( ) {
        return stringIndex;
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(tag, stringIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        return ((ConstantPoolEntryString) other).stringIndex == stringIndex;
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
