package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dreturn">dreturn</a>
 */
public class InstructionReturnDouble extends Instruction {
	protected InstructionReturnDouble( ) {
	}

	public static int[] getOpcodes( ) {
		return new int[] {0xAF};
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionReturnDouble load(int opcode, DataInputStream din) throws IOException {
			return new InstructionReturnDouble();
		}
	}
}
