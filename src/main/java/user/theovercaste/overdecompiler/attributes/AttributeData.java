package user.theovercaste.overdecompiler.attributes;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolValueRetriever;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class AttributeData {
	protected final int nameIndex;
	protected final byte[] data;

	public AttributeData(int nameIndex, byte[] data) {
		this.nameIndex = nameIndex;
		this.data = data;
	}

	public String getName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		return ConstantPoolValueRetriever.getInstance().getString(constantPool, nameIndex);
	}

	public static AttributeData readAttribute(DataInputStream din) throws IOException {
		int nameIndex = din.readUnsignedShort();
		byte[] data = new byte[din.readInt()];
		din.readFully(data);
		return new AttributeData(nameIndex, data);
	}

	public static abstract class Wrapper<T extends AttributeData> {
		public abstract T wrap(AttributeData a, ConstantPoolEntry[] constantPool);
	}
}
