package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

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
