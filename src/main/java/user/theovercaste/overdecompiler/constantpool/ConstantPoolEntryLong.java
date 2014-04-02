package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryLong extends ConstantPoolEntry {
	protected final long value;

	public ConstantPoolEntryLong(int tag, long value) {
		super(tag);
		this.value = value;
	}

	public long getValue( ) {
		return this.value;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends ConstantPoolEntry.Factory {
		protected long value;

		@Override
		public void read(int tag, DataInputStream din) throws IOException {
			super.read(tag, din);
			this.value = din.readLong();
		}

		@Override
		public ConstantPoolEntry build( ) {
			return new ConstantPoolEntryLong(this.tag, this.value);
		}
	}
}
