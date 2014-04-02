package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;

public class ConstantPoolEntryNameAndType extends ConstantPoolEntry {
	protected final int nameIndex;
	protected final int descriptorIndex;

	public ConstantPoolEntryNameAndType(int tag, int nameIndex, int descriptorIndex) {
		super(tag);
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
	}

	public int getNameIndex( ) {
		return this.nameIndex;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public String getName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(this.nameIndex, constantPool.length);
		ConstantPoolEntry e = constantPool[this.nameIndex];
		if (e instanceof ConstantPoolEntryUtf8) {
			return ((ConstantPoolEntryUtf8) e).toString();
		}
		throw PoolPreconditions.getInvalidType(constantPool, this.nameIndex);
	}

	public String getDescription(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(this.descriptorIndex, constantPool.length);
		ConstantPoolEntry e = constantPool[this.descriptorIndex];
		if (e instanceof ConstantPoolEntryUtf8) {
			return ((ConstantPoolEntryUtf8) e).toString();
		}
		throw PoolPreconditions.getInvalidType(constantPool, this.descriptorIndex);
	}

	public static class Factory extends ConstantPoolEntry.Factory {
		protected int nameIndex;
		protected int descriptorIndex;

		@Override
		public void read(int tag, DataInputStream din) throws IOException {
			super.read(tag, din);
			this.nameIndex = din.readUnsignedShort();
			this.descriptorIndex = din.readUnsignedShort();
		}

		@Override
		public ConstantPoolEntry build( ) {
			return new ConstantPoolEntryNameAndType(this.tag, this.nameIndex, this.descriptorIndex);
		}
	}
}
