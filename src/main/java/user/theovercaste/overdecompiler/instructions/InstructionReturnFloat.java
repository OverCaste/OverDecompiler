package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

public class InstructionReturnFloat extends Instruction {
	protected InstructionReturnFloat( ) {
	}

	public static int[] getOpcodes( ) {
		return new int[] {0xAE};
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionReturnFloat load(DataInputStream din) throws IOException {
			return new InstructionReturnFloat();
		}
	}
}
