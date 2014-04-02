package user.theovercaste.overdecompiler.datahandlers;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.attributes.Attribute;
import user.theovercaste.overdecompiler.attributes.Attributes;
import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.Field;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryUtf8;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;

public class FieldData {
	private final AccessFlagHandler flagHandler;
	private final int nameIndex;
	private final int descriptorIndex;
	private final Attribute[] attributes;

	public FieldData(AccessFlagHandler flagHandler, int nameIndex, int descriptorIndex, Attribute[] attributes) {
		this.flagHandler = flagHandler;
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
		this.attributes = attributes;
	}

	public AccessFlagHandler getFlagHandler( ) {
		return flagHandler;
	}

	public int getNameIndex( ) {
		return nameIndex;
	}

	public int getDescriptorIndex( ) {
		return descriptorIndex;
	}

	public Attribute[] getAttributes( ) {
		return attributes;
	}

	public String getName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(nameIndex, constantPool.length);
		ConstantPoolEntry e = constantPool[nameIndex];
		if (e instanceof ConstantPoolEntryUtf8) {
			return ((ConstantPoolEntryUtf8) e).toString();
		}
		throw PoolPreconditions.getInvalidType(constantPool, nameIndex);
	}

	public String getDescription(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(descriptorIndex, constantPool.length);
		ConstantPoolEntry e = constantPool[descriptorIndex];
		if (e instanceof ConstantPoolEntryUtf8) {
			return ((ConstantPoolEntryUtf8) e).toString();
		}
		throw PoolPreconditions.getInvalidType(constantPool, descriptorIndex);
	}

	public static FieldData loadFieldInfo(DataInputStream din) throws IOException {
		AccessFlagHandler flagHandler = new AccessFlagHandler(din.readUnsignedShort());
		int nameIndex = din.readUnsignedShort();
		int descriptorIndex = din.readUnsignedShort();
		Attribute[] attributes = new Attribute[din.readUnsignedShort()];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = Attributes.loadAttribute(din);
		}
		return new FieldData(flagHandler, nameIndex, descriptorIndex, attributes);
	}

	public Field toField(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		Field ret = new Field(getName(constantPool), new ClassPath("java/lang/Object"), flagHandler);
		return ret;
	}
}
