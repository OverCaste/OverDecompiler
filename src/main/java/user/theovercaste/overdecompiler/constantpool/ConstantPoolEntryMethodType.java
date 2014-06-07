package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ConstantPoolEntryMethodType extends ConstantPoolEntry {
    protected final int descriptorIndex;

    public ConstantPoolEntryMethodType(int descriptorIndex) {
        super(ConstantPoolEntries.METHOD_TYPE_TAG);
        this.descriptorIndex = descriptorIndex;
    }

    public int getDescriptorIndex( ) {
        return descriptorIndex;
    }

    @Override
    public void write(DataOutputStream dout) throws IOException {
        dout.writeShort(descriptorIndex);
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(tag, descriptorIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        return (((ConstantPoolEntryMethodType) other).descriptorIndex == descriptorIndex);
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int descriptorIndex;

        @Override
        public void read(DataInputStream din) throws IOException {
            descriptorIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryMethodType(descriptorIndex);
        }
    }
}
