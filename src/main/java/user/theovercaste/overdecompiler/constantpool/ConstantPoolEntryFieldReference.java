package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;

public class ConstantPoolEntryFieldReference extends ConstantPoolEntry {
	protected final int classIndex;
	protected final int nameAndTypeIndex;

	public ConstantPoolEntryFieldReference(int tag, int classIndex, int nameAndTypeIndex) {
		super(tag);
		this.classIndex = classIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	public int getClassIndex( ) {
		return this.classIndex;
	}

	public int getNameAndTypeIndex( ) {
		return this.nameAndTypeIndex;
	}

	public String getName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(this.nameAndTypeIndex, constantPool.length);
		ConstantPoolEntry entry = constantPool[this.nameAndTypeIndex];
		if (entry instanceof ConstantPoolEntryNameAndType) {
			return ((ConstantPoolEntryNameAndType) entry).getName(constantPool);
		}
		throw PoolPreconditions.getInvalidType(constantPool, this.nameAndTypeIndex);
	}

	public String getClassName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(this.classIndex, constantPool.length);
		ConstantPoolEntry entry = constantPool[this.classIndex];
		if (entry instanceof ConstantPoolEntryClass) {
			return ((ConstantPoolEntryClass) entry).getName(constantPool);
		}
		throw PoolPreconditions.getInvalidType(constantPool, this.classIndex);
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
			this.classIndex = din.readUnsignedShort();
			this.nameAndTypeIndex = din.readUnsignedShort();
		}

		@Override
		public ConstantPoolEntry build( ) {
			return new ConstantPoolEntryFieldReference(this.tag, this.classIndex, this.nameAndTypeIndex);
		}
	}
}
