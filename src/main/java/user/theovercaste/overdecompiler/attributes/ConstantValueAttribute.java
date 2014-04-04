package user.theovercaste.overdecompiler.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;

public class ConstantValueAttribute extends AttributeData {
	private final int attributeIndex;

	public ConstantValueAttribute(int nameIndex, byte[] data, int attributeIndex) {
		super(nameIndex, data);
		this.attributeIndex = attributeIndex;
	}

	public int getAttributeIndex( ) {
		return this.attributeIndex;
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

	public static class Wrapper extends AttributeData.Wrapper<ConstantValueAttribute> {
		@Override
		public ConstantValueAttribute wrap(AttributeData a, ConstantPoolEntry[] constantPool) {
			try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.data))) {
				return new ConstantValueAttribute(a.nameIndex, a.data, din.readUnsignedShort());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
