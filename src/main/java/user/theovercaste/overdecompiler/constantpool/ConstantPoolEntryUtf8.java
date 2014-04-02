package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class ConstantPoolEntryUtf8 extends ConstantPoolEntry {
	protected final byte[] data;

	public ConstantPoolEntryUtf8(int tag, byte[] data) {
		super(tag);
		this.data = data;
	}

	public byte[] getData( ) {
		return this.data;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	@Override
	public String toString( ) {
		try {
			return new String(this.data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static class Factory extends ConstantPoolEntry.Factory {
		protected byte[] data;
		protected int length;

		@Override
		public void read(int tag, DataInputStream din) throws IOException {
			super.read(tag, din);
			this.length = din.readUnsignedShort();
			this.data = new byte[this.length];
			din.readFully(this.data);
		}

		@Override
		public ConstantPoolEntry build( ) {
			return new ConstantPoolEntryUtf8(this.tag, this.data);
		}
	}
}
