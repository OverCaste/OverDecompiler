package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ConstantPoolEntryInterfaceMethodReference extends ConstantPoolEntry implements ConstantPoolEntryReference {
    protected final int classIndex;
    protected final int nameAndTypeIndex;

    public ConstantPoolEntryInterfaceMethodReference(int classIndex, int nameAndTypeIndex) {
        super(ConstantPoolEntries.METHOD_REFERENCE_TAG);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public int getClassIndex( ) {
        return classIndex;
    }

    @Override
    public int getNameAndTypeIndex( ) {
        return nameAndTypeIndex;
    }

    @Override
    public void write(DataOutputStream dout) throws IOException {
        dout.writeShort(classIndex);
        dout.writeShort(nameAndTypeIndex);
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(tag, classIndex, nameAndTypeIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        return (((ConstantPoolEntryInterfaceMethodReference) other).classIndex == classIndex) && (((ConstantPoolEntryInterfaceMethodReference) other).nameAndTypeIndex == nameAndTypeIndex);
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int classIndex;
        protected int nameAndTypeIndex;

        @Override
        public void read(DataInputStream din) throws IOException {
            classIndex = din.readUnsignedShort();
            nameAndTypeIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryInterfaceMethodReference(classIndex, nameAndTypeIndex);
        }
    }
}
