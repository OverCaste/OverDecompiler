package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryClass extends ConstantPoolEntry {
	protected final int nameIndex;

	public ConstantPoolEntryClass(int tag, int nameIndex) {
		super(tag);
		this.nameIndex = nameIndex;
	}

	public int getNameIndex( ) {
		return this.nameIndex;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public String getName(ConstantPoolEntry[] constantPool) {
		ConstantPoolEntry name = constantPool[this.nameIndex];
		return name.toString();
	}

	public static class Factory extends ConstantPoolEntry.Factory {
		protected int nameIndex;

		@Override
		public void read(int tag, DataInputStream din) throws IOException {
			super.read(tag, din);
			this.nameIndex = din.readUnsignedShort();
		}

		@Override
		public ConstantPoolEntry build( ) {
			return new ConstantPoolEntryClass(this.tag, this.nameIndex);
		}
	}
}
