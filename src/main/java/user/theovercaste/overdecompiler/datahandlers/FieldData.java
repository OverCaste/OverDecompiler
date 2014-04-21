package user.theovercaste.overdecompiler.datahandlers;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.attributes.Attributes;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolValueRetriever;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class FieldData {
	private final FieldFlagHandler flagHandler;
	private final int nameIndex;
	private final int descriptorIndex;
	private final AttributeData[] attributes;

	public FieldData(FieldFlagHandler flagHandler, int nameIndex, int descriptorIndex, AttributeData[] attributes) {
		this.flagHandler = flagHandler;
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
		this.attributes = attributes;
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

	public AttributeData[] getAttributes( ) {
		return attributes;
	}

	public String getName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		return ConstantPoolValueRetriever.getInstance().getString(constantPool, nameIndex);
	}

	public String getDescription(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		return ConstantPoolValueRetriever.getInstance().getString(constantPool, descriptorIndex);
	}

	public static FieldData loadFieldInfo(DataInputStream din) throws IOException {
		FieldFlagHandler flagHandler = new FieldFlagHandler(din.readUnsignedShort());
		int nameIndex = din.readUnsignedShort();
		int descriptorIndex = din.readUnsignedShort();
		AttributeData[] attributes = new AttributeData[din.readUnsignedShort()];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = Attributes.loadAttribute(din);
		}
		return new FieldData(flagHandler, nameIndex, descriptorIndex, attributes);
	}
}
