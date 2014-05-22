package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class ConstantPoolEntryNameAndType extends ConstantPoolEntry {
    protected final int nameIndex;
    protected final int descriptorIndex;

    public ConstantPoolEntryNameAndType(int tag, int nameIndex, int descriptorIndex) {
        super(tag);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public int getNameIndex( ) {
        return nameIndex;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public String getName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
        return ConstantPoolValueRetriever.getString(constantPool, nameIndex);
    }

    public String getDescription(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
        return ConstantPoolValueRetriever.getString(constantPool, descriptorIndex);
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int nameIndex;
        protected int descriptorIndex;

        @Override
        public void read(int tag, DataInputStream din) throws IOException {
            super.read(tag, din);
            nameIndex = din.readUnsignedShort();
            descriptorIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryNameAndType(tag, nameIndex, descriptorIndex);
        }
    }
}
