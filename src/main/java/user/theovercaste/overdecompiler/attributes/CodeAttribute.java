package user.theovercaste.overdecompiler.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;

public class CodeAttribute extends Attribute {
	private final int maxStacks;
	private final int maxLocals;
	private final byte[] code;
	private final ExceptionAttribute[] exceptions;
	private final Attribute[] attributes;

	public CodeAttribute(int nameIndex, byte[] data, int maxStacks, int maxLocals, byte[] code, ExceptionAttribute[] exceptions, Attribute[] attributes) {
		super(nameIndex, data);
		this.maxStacks = maxStacks;
		this.maxLocals = maxLocals;
		this.code = code;
		this.exceptions = exceptions;
		this.attributes = attributes;
	}

	public int getMaxStacks( ) {
		return this.maxStacks;
	}

	public int getMaxLocals( ) {
		return this.maxLocals;
	}

	public byte[] getCode( ) {
		return this.code;
	}

	public ExceptionAttribute[] getExceptions( ) {
		return this.exceptions;
	}

	public Attribute[] getAttributes( ) {
		return this.attributes;
	}

	public static Wrapper wrapper( ) {
		return new Wrapper();
	}

	public static class ExceptionAttribute {
		protected final int startPc;
		protected final int endPc;
		protected final int handlerPc;
		protected final int catchType;

		public ExceptionAttribute(int startPc, int endPc, int handlerPc, int catchType) {
			this.startPc = startPc;
			this.endPc = endPc;
			this.handlerPc = handlerPc;
			this.catchType = catchType;
		}
	}

	public static class Wrapper extends Attribute.Wrapper<CodeAttribute> {
		@Override
		public CodeAttribute wrap(Attribute a, ConstantPoolEntry[] constantPool) {
			try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.data))) {
				int maxStack = din.readUnsignedShort();
				int maxLocals = din.readUnsignedShort();
				byte[] code = new byte[din.readInt()];
				din.readFully(code);
				ExceptionAttribute[] exceptions = new ExceptionAttribute[din.readUnsignedShort()];
				for (int i = 0; i < exceptions.length; i++) {
					exceptions[i] = new ExceptionAttribute(din.readUnsignedShort(), din.readUnsignedShort(), din.readUnsignedShort(), din.readUnsignedShort());
				}
				Attribute[] attributes = new Attribute[din.readUnsignedShort()];
				for (int i = 0; i < attributes.length; i++) {
					attributes[i] = Attributes.loadAttribute(din);
				}
				return new CodeAttribute(a.nameIndex, a.data, maxStack, maxLocals, code, exceptions, attributes);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
