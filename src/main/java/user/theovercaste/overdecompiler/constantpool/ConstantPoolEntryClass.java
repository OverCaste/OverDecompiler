package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

public class ConstantPoolEntryClass extends ConstantPoolEntry {
    protected final int nameIndex;

    public ConstantPoolEntryClass(int nameIndex) {
        super(ConstantPoolEntries.CLASS_TAG);
        this.nameIndex = nameIndex;
    }

    public int getNameIndex( ) {
        return nameIndex;
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(tag, nameIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        return ((ConstantPoolEntryClass) other).nameIndex == nameIndex;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int nameIndex;

        @Override
        public void read(DataInputStream din) throws IOException {
            nameIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryClass(nameIndex);
        }
    }
}
