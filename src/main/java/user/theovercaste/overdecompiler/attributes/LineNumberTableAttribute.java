package user.theovercaste.overdecompiler.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;

public class LineNumberTableAttribute extends AttributeData {
	private final LineNumberTableValue[] table;

	public LineNumberTableAttribute(int nameIndex, byte[] data, LineNumberTableValue[] table) {
		super(nameIndex, data);
		this.table = table;
	}

	public LineNumberTableValue[] getTable( ) {
		return table;
	}

	public static Wrapper wrapper( ) {
		return new Wrapper();
	}

	public static class LineNumberTableValue {
		protected final int startPc;
		protected final int lineNumber;

		public LineNumberTableValue(int startPc, int lineNumber) {
			this.startPc = startPc;
			this.lineNumber = lineNumber;
		}
	}

	public static class Wrapper extends AttributeData.Wrapper<LineNumberTableAttribute> {
		@Override
		public LineNumberTableAttribute wrap(AttributeData a, ConstantPoolEntry[] constantPool) {
			try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.data))) {
				LineNumberTableValue[] table = new LineNumberTableValue[din.readUnsignedShort()];
				for (int i = 0; i < table.length; i++) {
					table[i] = new LineNumberTableValue(din.readUnsignedShort(), din.readUnsignedShort());
				}
				return new LineNumberTableAttribute(a.nameIndex, a.data, table);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
