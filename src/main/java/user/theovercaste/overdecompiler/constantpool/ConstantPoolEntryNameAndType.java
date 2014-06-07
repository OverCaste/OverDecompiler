package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ConstantPoolEntryNameAndType extends ConstantPoolEntry {
    protected final int nameIndex;
    protected final int descriptorIndex;

    public ConstantPoolEntryNameAndType(int nameIndex, int descriptorIndex) {
        super(ConstantPoolEntries.NAME_AND_TYPE_TAG);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public int getNameIndex( ) {
        return nameIndex;
    }

    @Override
    public void write(DataOutputStream dout) throws IOException {
        dout.writeShort(nameIndex);
        dout.writeShort(descriptorIndex);
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(tag, nameIndex, descriptorIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        return (((ConstantPoolEntryNameAndType) other).nameIndex == nameIndex) && (((ConstantPoolEntryNameAndType) other).descriptorIndex == descriptorIndex);
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int nameIndex;
        protected int descriptorIndex;

        @Override
        public void read(DataInputStream din) throws IOException {
            nameIndex = din.readUnsignedShort();
            descriptorIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryNameAndType(nameIndex, descriptorIndex);
        }
    }
}
