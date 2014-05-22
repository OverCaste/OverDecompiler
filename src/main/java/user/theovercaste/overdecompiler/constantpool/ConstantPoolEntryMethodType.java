package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class ConstantPoolEntryMethodType extends ConstantPoolEntry {
    protected final int descriptorIndex;

    public ConstantPoolEntryMethodType(int tag, int descriptorIndex) {
        super(tag);
        this.descriptorIndex = descriptorIndex;
    }

    public int getDescriptorIndex( ) {
        return descriptorIndex;
    }

    public String getDescription(ConstantPool constantPool) throws InvalidConstantPoolPointerException {
        return ConstantPoolValueRetriever.getString(constantPool, descriptorIndex);
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int descriptorIndex;

        @Override
        public void read(int tag, DataInputStream din) throws IOException {
            super.read(tag, din);
            descriptorIndex = din.readInt();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryMethodType(tag, descriptorIndex);
        }
    }
}
