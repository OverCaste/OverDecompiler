package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryMethodType extends ConstantPoolEntry {
    protected final int descriptorIndex;

    public ConstantPoolEntryMethodType(int descriptorIndex) {
        super(ConstantPoolEntries.METHOD_TYPE_TAG);
        this.descriptorIndex = descriptorIndex;
    }

    public int getDescriptorIndex( ) {
        return descriptorIndex;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int descriptorIndex;

        @Override
        public void read(DataInputStream din) throws IOException {
            descriptorIndex = din.readInt();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryMethodType(descriptorIndex);
        }
    }
}
