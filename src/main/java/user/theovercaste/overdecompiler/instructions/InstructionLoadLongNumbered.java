package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lload_n">lload_n</a>
 */
public class InstructionLoadLongNumbered extends Instruction {
	private final int id;

	public InstructionLoadLongNumbered(int opcode) {
		int id;
		switch (opcode) {
			case 0x1e:
				id = 0;
				break;
			case 0x1f:
				id = 1;
				break;
			case 0x20:
				id = 2;
				break;
			case 0x21:
				id = 3;
				break;
			default:
				throw new IllegalArgumentException("opcode is an invalid value: " + opcode + ", expected: " + Joiner.on(",").join(Ints.asList(getOpcodes())));
		}
		this.id = id;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x1e, 0x1f, 0x20, 0x21};
	}

	public int getId( ) {
		return id;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadLongNumbered load(int opcode, DataInputStream din) throws IOException {
			return new InstructionLoadLongNumbered(opcode);
		}
	}
}
