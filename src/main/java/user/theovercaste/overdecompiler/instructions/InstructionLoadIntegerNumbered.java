package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iload_n">iload_n</a>
 */
public class InstructionLoadIntegerNumbered extends Instruction {
	private final int id;

	public InstructionLoadIntegerNumbered(int opcode) {
		int id;
		switch (opcode) {
			case 0x1a:
				id = 0;
				break;
			case 0x1b:
				id = 1;
				break;
			case 0x1c:
				id = 2;
				break;
			case 0x1d:
				id = 3;
				break;
			default:
				throw new IllegalArgumentException("opcode is an invalid value: " + opcode + ", expected: " + Joiner.on(",").join(Ints.asList(getOpcodes())));
		}
		this.id = id;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x1a, 0x1b, 0x1c, 0x1d};
	}

	public int getId( ) {
		return id;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadIntegerNumbered load(int opcode, DataInputStream din) throws IOException {
			return new InstructionLoadIntegerNumbered(opcode);
		}
	}
}
