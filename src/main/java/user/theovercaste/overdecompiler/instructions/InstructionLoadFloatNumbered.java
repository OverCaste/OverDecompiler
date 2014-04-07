package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fload_n">fload_n</a>
 */
public class InstructionLoadFloatNumbered extends Instruction {
	private final int id;

	public InstructionLoadFloatNumbered(int opcode) {
		int id;
		switch (opcode) {
			case 0x22:
				id = 0;
				break;
			case 0x23:
				id = 1;
				break;
			case 0x24:
				id = 2;
				break;
			case 0x25:
				id = 3;
				break;
			default:
				throw new IllegalArgumentException("opcode is an invalid value: " + opcode + ", expected: " + Joiner.on(",").join(Ints.asList(getOpcodes())));
		}
		this.id = id;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x22, 0x23, 0x24, 0x25};
	}

	public int getId( ) {
		return id;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadFloatNumbered load(int opcode, DataInputStream din) throws IOException {
			return new InstructionLoadFloatNumbered(opcode);
		}
	}
}
