package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.base.Joiner;
import com.google.common.primitives.Ints;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dload_n">dload_n</a>
 */
public class InstructionLoadDoubleNumbered extends Instruction {
	private final int id;

	public InstructionLoadDoubleNumbered(int opcode) {
		int id;
		switch (opcode) {
			case 0x26:
				id = 0;
				break;
			case 0x27:
				id = 1;
				break;
			case 0x28:
				id = 2;
				break;
			case 0x29:
				id = 3;
				break;
			default:
				throw new IllegalArgumentException("opcode is an invalid value: " + opcode + ", expected: " + Joiner.on(",").join(Ints.asList(getOpcodes())));
		}
		this.id = id;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x26, 0x27, 0x28, 0x29};
	}

	public int getId( ) {
		return id;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadDoubleNumbered load(int opcode, DataInputStream din) throws IOException {
			return new InstructionLoadDoubleNumbered(opcode);
		}
	}
}
