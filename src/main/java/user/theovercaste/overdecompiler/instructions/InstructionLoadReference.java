package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aload">aload</a>
 */
public class InstructionLoadReference extends Instruction {
	private final int referenceIndex;

	public InstructionLoadReference(int referenceIndex) {
		this.referenceIndex = referenceIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x19};
	}

	public int getNumber( ) {
		return referenceIndex;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadReference load(int opcode, DataInputStream din) throws IOException {
			int referenceIndex = din.readUnsignedByte();
			return new InstructionLoadReference(referenceIndex);
		}
	}
}
