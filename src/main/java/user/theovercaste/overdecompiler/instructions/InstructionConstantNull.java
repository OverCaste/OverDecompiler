package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aconst_null">aconst_null</a>
 */
public class InstructionConstantNull extends Instruction {
	private final int index;

	public InstructionConstantNull(int referenceIndex) {
		index = referenceIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x1};
	}

	public int getIndex( ) {
		return index;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionConstantNull load(int opcode, DataInputStream din) throws IOException {
			int index = din.readUnsignedByte();
			return new InstructionConstantNull(index);
		}
	}
}
