package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

public class ConstantPoolEntryMethodType extends ConstantPoolEntry {
	protected final int descriptorIndex;

	public ConstantPoolEntryMethodType(int tag, int descriptorIndex) {
		super(tag);
		this.descriptorIndex = descriptorIndex;
	}

	public int getDescriptorIndex( ) {
		return this.descriptorIndex;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends ConstantPoolEntry.Factory {
		protected int descriptorIndex;

		@Override
		public void read(int tag, DataInputStream din) throws IOException {
			super.read(tag, din);
			this.descriptorIndex = din.readInt();
		}

		@Override
		public ConstantPoolEntry build( ) {
			return new ConstantPoolEntryMethodType(this.tag, this.descriptorIndex);
		}
	}
}
