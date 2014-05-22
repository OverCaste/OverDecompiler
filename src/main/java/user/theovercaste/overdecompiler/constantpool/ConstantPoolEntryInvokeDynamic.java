package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class ConstantPoolEntryInvokeDynamic extends ConstantPoolEntry {
    protected final int methodAttributeIndex;
    protected final int nameAndTypeIndex;

    public ConstantPoolEntryInvokeDynamic(int tag, int methodAttributeIndex, int nameAndTypeIndex) {
        super(tag);
        this.methodAttributeIndex = methodAttributeIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public int getMethodAttributeIndex( ) {
        return methodAttributeIndex;
    }

    public int getNameAndTypeIndex( ) {
        return nameAndTypeIndex;
    }

    public String getName(ConstantPool constantPool) throws InvalidConstantPoolPointerException {
        return ConstantPoolValueRetriever.getNameAndTypeName(constantPool, nameAndTypeIndex);
    }

    public String getDescription(ConstantPool constantPool) throws InvalidConstantPoolPointerException {
        return ConstantPoolValueRetriever.getNameAndTypeDescription(constantPool, nameAndTypeIndex);
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int methodAttributeIndex;
        protected int nameAndTypeIndex;

        @Override
        public void read(int tag, DataInputStream din) throws IOException {
            super.read(tag, din);
            methodAttributeIndex = din.readUnsignedShort();
            nameAndTypeIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryInvokeDynamic(tag, methodAttributeIndex, nameAndTypeIndex);
        }
    }
}
