package user.theovercaste.overdecompiler.datahandlers;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import user.theovercaste.overdecompiler.attributes.AttributableElement;
import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolValueRetriever;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

import com.google.common.collect.Lists;

public class FieldData implements AttributableElement {
    private final FieldFlagHandler flagHandler;
    private final int nameIndex;
    private final int descriptorIndex;
    private final List<AttributeData> attributes = Lists.newArrayList();

    public FieldData(FieldFlagHandler flagHandler, int nameIndex, int descriptorIndex) {
        this.flagHandler = flagHandler;
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    @Override
    public void addAttribute(AttributeData d) {
        attributes.add(d);
    }

    public FieldFlagHandler getFlagHandler( ) {
        return flagHandler;
    }

    public int getNameIndex( ) {
        return nameIndex;
    }

    public int getDescriptorIndex( ) {
        return descriptorIndex;
    }

    @Override
    public Collection<AttributeData> getAttributes( ) {
        return Collections.unmodifiableCollection(attributes);
    }

    public String getName(ConstantPool constantPool) throws InvalidConstantPoolPointerException {
        return ConstantPoolValueRetriever.getString(constantPool, nameIndex);
    }

    public String getDescription(ConstantPool constantPool) throws InvalidConstantPoolPointerException {
        return ConstantPoolValueRetriever.getString(constantPool, descriptorIndex);
    }

    public static FieldData loadFieldInfo(DataInputStream din) throws IOException {
        FieldFlagHandler flagHandler = new FieldFlagHandler(din.readUnsignedShort());
        int nameIndex = din.readUnsignedShort();
        int descriptorIndex = din.readUnsignedShort();
        int attributeCount = din.readUnsignedShort();
        FieldData ret = new FieldData(flagHandler, nameIndex, descriptorIndex);
        for (int i = 0; i < attributeCount; i++) {
            ret.addAttribute(AttributeData.loadAttribute(din));
        }
        return ret;
    }
}
