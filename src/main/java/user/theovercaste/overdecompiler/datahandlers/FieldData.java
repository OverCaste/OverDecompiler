package user.theovercaste.overdecompiler.datahandlers;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

import com.google.common.collect.ImmutableList;

public class FieldData {
    private final int flagMask;
    private final int nameIndex;
    private final int descriptorIndex;
    private final ImmutableList<AttributeData> attributes;

    public FieldData(int flagMask, int nameIndex, int descriptorIndex, ImmutableList<AttributeData> attributes) {
        this.flagMask = flagMask;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
        this.attributes = attributes;
    }

    public int getFlagMask( ) {
        return flagMask;
    }

    public int getNameIndex( ) {
        return nameIndex;
    }

    public int getDescriptorIndex( ) {
        return descriptorIndex;
    }

    public ImmutableList<AttributeData> getAttributes( ) {
        return attributes;
    }

    public String getName(ConstantPool constantPool) throws InvalidConstantPoolPointerException {
        return constantPool.getUtf8(nameIndex);
    }

    public String getDescription(ConstantPool constantPool) throws InvalidConstantPoolPointerException {
        return constantPool.getUtf8(descriptorIndex);
    }

    public static FieldData loadField(DataInputStream din) throws IOException {
        int flagMask = din.readUnsignedShort();
        int nameIndex = din.readUnsignedShort();
        int descriptorIndex = din.readUnsignedShort();
        int attributeCount = din.readUnsignedShort();
        ImmutableList.Builder<AttributeData> attributeBuilder = new ImmutableList.Builder<AttributeData>();
        for (int i = 0; i < attributeCount; i++) {
            attributeBuilder.add(AttributeData.loadAttribute(din));
        }
        return new FieldData(flagMask, nameIndex, descriptorIndex, attributeBuilder.build());
    }
}
