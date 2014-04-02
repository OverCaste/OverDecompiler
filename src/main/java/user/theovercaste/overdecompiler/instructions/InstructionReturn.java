package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

public class InstructionReturn extends Instruction {
	protected InstructionReturn( ) {
	}

	public static int[] getOpcodes( ) {
		return new int[] {0xb1};
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionReturn load(DataInputStream din) throws IOException {
			return new InstructionReturn();
		}
	}
}
