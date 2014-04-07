package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fload">fload</a>
 */
public class InstructionLoadFloat extends Instruction {
	private final int index;

	public InstructionLoadFloat(int referenceIndex) {
		index = referenceIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x17};
	}

	public int getIndex( ) {
		return index;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadFloat load(int opcode, DataInputStream din) throws IOException {
			int index = din.readUnsignedByte();
			return new InstructionLoadFloat(index);
		}
	}
}
