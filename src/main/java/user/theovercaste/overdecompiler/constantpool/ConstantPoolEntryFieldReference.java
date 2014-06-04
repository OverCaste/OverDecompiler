package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryFieldReference extends ConstantPoolEntry implements ConstantPoolEntryReference {
    protected final int classIndex;
    protected final int nameAndTypeIndex;

    public ConstantPoolEntryFieldReference(int classIndex, int nameAndTypeIndex) {
        super(ConstantPoolEntries.FIELD_REFERENCE_TAG);
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
            return new ConstantPoolEntryFieldReference(classIndex, nameAndTypeIndex);
        }
    }
}
