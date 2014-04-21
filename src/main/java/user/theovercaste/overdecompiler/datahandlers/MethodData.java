package user.theovercaste.overdecompiler.datahandlers;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.attributes.Attributes;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolValueRetriever;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class MethodData {
	private final MethodFlagHandler flags;
	private final int nameIndex;
	private final int descriptorIndex;
	private final AttributeData[] attributes;

	public MethodData(MethodFlagHandler flags, int nameIndex, int descriptorIndex, AttributeData[] attributes) {
		this.flags = flags;
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
		this.attributes = attributes;
	}

	public MethodFlagHandler getFlagHandler( ) {
		return flags;
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

	public static MethodData loadMethodData(DataInputStream din) throws IOException {
		MethodFlagHandler flagHandler = new MethodFlagHandler(din.readUnsignedShort());
		int nameIndex = din.readUnsignedShort();
		int descriptorIndex = din.readUnsignedShort();
		AttributeData[] attributes = new AttributeData[din.readUnsignedShort()];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = Attributes.loadAttribute(din);
		}
		return new MethodData(flagHandler, nameIndex, descriptorIndex, attributes);
	}
}
