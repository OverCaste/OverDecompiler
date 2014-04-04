package user.theovercaste.overdecompiler.attributes;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryUtf8;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;

public class AttributeData {
	protected final int nameIndex;
	protected final byte[] data;

	public AttributeData(int nameIndex, byte[] data) {
		this.nameIndex = nameIndex;
		this.data = data;
	}

	public String getName(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(this.nameIndex, constantPool.length);
		ConstantPoolEntry e = constantPool[this.nameIndex];
		if (e instanceof ConstantPoolEntryUtf8) {
			return ((ConstantPoolEntryUtf8) e).toString();
		}
		throw PoolPreconditions.getInvalidType(constantPool, this.nameIndex);
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
