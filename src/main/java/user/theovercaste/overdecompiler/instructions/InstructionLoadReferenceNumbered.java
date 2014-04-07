package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aload_n">aload_n</a>
 */
public class InstructionLoadReferenceNumbered extends Instruction {
	private final int id;

	public InstructionLoadReferenceNumbered(int opcode) {
		int id;
		switch (opcode) {
			case 0x2A:
				id = 0;
				break;
			case 0x2B:
				id = 1;
				break;
			case 0x2C:
				id = 2;
				break;
			case 0x2D:
				id = 3;
				break;
			default:
				throw new IllegalArgumentException("opcode is an invalid value: " + opcode + ", expected: " + Joiner.on(",").join(Ints.asList(getOpcodes())));
		}
		this.id = id;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x2A, 0x2B, 0x2C, 0x2D};
	}

	public int getId( ) {
		return id;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadReferenceNumbered load(int opcode, DataInputStream din) throws IOException {
			return new InstructionLoadReferenceNumbered(opcode);
		}
	}
}
