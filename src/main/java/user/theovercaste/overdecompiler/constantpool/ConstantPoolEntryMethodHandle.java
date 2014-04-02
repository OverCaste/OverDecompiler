package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryMethodHandle extends ConstantPoolEntry {
	protected final int referenceKind;
	protected final int referenceIndex;

	public ConstantPoolEntryMethodHandle(int tag, int referenceKind, int referenceIndex) {
		super(tag);
		this.referenceKind = referenceKind;
		this.referenceIndex = referenceIndex;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public int getReferenceKind( ) {
		return this.referenceKind;
	}

	public int getReferenceIndex( ) {
		return this.referenceIndex;
	}

	public static class Factory extends ConstantPoolEntry.Factory {
		protected int referenceKind;
		protected int referenceIndex;

		@Override
		public void read(int tag, DataInputStream din) throws IOException {
			super.read(tag, din);
			this.referenceKind = din.readUnsignedByte();
			this.referenceIndex = din.readUnsignedShort();
		}

		@Override
		public ConstantPoolEntry build( ) {
			return new ConstantPoolEntryMethodHandle(this.tag, this.referenceKind, this.referenceIndex);
		}
	}
}
