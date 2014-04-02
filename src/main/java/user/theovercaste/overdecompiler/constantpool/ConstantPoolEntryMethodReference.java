package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryMethodReference extends ConstantPoolEntry {
	protected final int classIndex;
	protected final int nameAndTypeIndex;

	public ConstantPoolEntryMethodReference(int tag, int classIndex, int nameAndTypeIndex) {
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
			return new ConstantPoolEntryMethodReference(this.tag, this.classIndex, this.nameAndTypeIndex);
		}
	}
}
