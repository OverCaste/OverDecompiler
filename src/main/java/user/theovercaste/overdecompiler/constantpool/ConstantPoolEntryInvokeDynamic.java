package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ConstantPoolEntryInvokeDynamic extends ConstantPoolEntry {
    protected final int methodAttributeIndex;
    protected final int nameAndTypeIndex;

    public ConstantPoolEntryInvokeDynamic(int methodAttributeIndex, int nameAndTypeIndex) {
        super(ConstantPoolEntries.INVOKE_DYNAMIC_TAG);
        this.methodAttributeIndex = methodAttributeIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getMethodAttributeIndex( ) {
        return methodAttributeIndex;
    }

    public int getNameAndTypeIndex( ) {
        return nameAndTypeIndex;
    }

    @Override
    public void write(DataOutputStream dout) throws IOException {
        dout.writeShort(methodAttributeIndex);
        dout.writeShort(nameAndTypeIndex);
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(tag, methodAttributeIndex, nameAndTypeIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        return (((ConstantPoolEntryInvokeDynamic) other).methodAttributeIndex == methodAttributeIndex) && (((ConstantPoolEntryInvokeDynamic) other).nameAndTypeIndex == nameAndTypeIndex);
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int methodAttributeIndex;
        protected int nameAndTypeIndex;

        @Override
        public void read(DataInputStream din) throws IOException {
            methodAttributeIndex = din.readUnsignedShort();
            nameAndTypeIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryInvokeDynamic(methodAttributeIndex, nameAndTypeIndex);
        }
    }
}
