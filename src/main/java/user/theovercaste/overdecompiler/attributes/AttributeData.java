package user.theovercaste.overdecompiler.attributes;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class AttributeData {
    protected final int nameIndex;
    protected final byte[] data;

    public AttributeData(int nameIndex, byte[] data) {
        this.nameIndex = nameIndex;
        this.data = data;
    }

    public String getName(ConstantPool constantPool) throws InvalidConstantPoolPointerException {
        return constantPool.getUtf8(nameIndex);
    }

    public static AttributeData loadAttribute(DataInputStream din) throws IOException {
        int nameIndex = din.readUnsignedShort();
        byte[] data = new byte[din.readInt()];
        din.readFully(data);
        return new AttributeData(nameIndex, data);
    }
}
