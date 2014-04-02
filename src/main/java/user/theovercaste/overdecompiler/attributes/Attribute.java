package user.theovercaste.overdecompiler.attributes;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryUtf8;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;

public class Attribute {
	protected final int nameIndex;
	protected final byte[] data;

	public Attribute(int nameIndex, byte[] data) {
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

	public static Attribute readAttribute(DataInputStream din) throws IOException {
		int nameIndex = din.readUnsignedShort();
		byte[] data = new byte[din.readInt()];
		din.readFully(data);
		return new Attribute(nameIndex, data);
	}

	public static abstract class Wrapper<T extends Attribute> {
		public abstract T wrap(Attribute a, ConstantPoolEntry[] constantPool);
	}
}
