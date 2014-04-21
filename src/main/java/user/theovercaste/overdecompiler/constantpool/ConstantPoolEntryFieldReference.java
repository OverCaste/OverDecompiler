package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class ConstantPoolEntryFieldReference extends ConstantPoolEntry {
	protected final int classIndex;
	protected final int nameAndTypeIndex;

	public ConstantPoolEntryFieldReference(int tag, int classIndex, int nameAndTypeIndex) {
		super(tag);
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	public int getClassIndex( ) {
		return classIndex;
	}

	public int getNameAndTypeIndex( ) {
		return nameAndTypeIndex;
	}

	public String getName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		return ConstantPoolValueRetriever.getInstance().getNameAndTypeName(constantPool, nameAndTypeIndex);
	}

	public String getDescription(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		return ConstantPoolValueRetriever.getInstance().getNameAndTypeDescription(constantPool, nameAndTypeIndex);
	}

	public String getClassName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		return ConstantPoolValueRetriever.getInstance().getClassName(constantPool, classIndex);
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends ConstantPoolEntry.Factory {
		protected int classIndex;
		protected int nameAndTypeIndex;

		@Override
		public void read(int tag, DataInputStream din) throws IOException {
			super.read(tag, din);
			classIndex = din.readUnsignedShort();
			nameAndTypeIndex = din.readUnsignedShort();
		}

		@Override
		public ConstantPoolEntry build( ) {
			return new ConstantPoolEntryFieldReference(tag, classIndex, nameAndTypeIndex);
		}
	}
}
