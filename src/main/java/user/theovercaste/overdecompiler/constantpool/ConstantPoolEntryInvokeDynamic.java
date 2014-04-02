package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryInvokeDynamic extends ConstantPoolEntry {
	protected final int methodAttributeIndex;
	protected final int nameAndTypeIndex;

	public ConstantPoolEntryInvokeDynamic(int tag, int methodAttributeIndex, int nameAndTypeIndex) {
		super(tag);
		this.methodAttributeIndex = methodAttributeIndex;
		this.nameAndTypeIndex = nameAndTypeIndex;
	}

	public int getMethodAttributeIndex( ) {
		return this.methodAttributeIndex;
	}

	public int getNameAndTypeIndex( ) {
		return this.nameAndTypeIndex;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends ConstantPoolEntry.Factory {
		protected int methodAttributeIndex;
		protected int nameAndTypeIndex;

		@Override
		public void read(int tag, DataInputStream din) throws IOException {
			super.read(tag, din);
			this.methodAttributeIndex = din.readUnsignedShort();
			this.nameAndTypeIndex = din.readUnsignedShort();
		}

		@Override
		public ConstantPoolEntry build( ) {
			return new ConstantPoolEntryInvokeDynamic(this.tag, this.methodAttributeIndex, this.nameAndTypeIndex);
		}
	}
}
