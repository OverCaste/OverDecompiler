package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.instructiontypes.ArrayStore;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.sastore">sastore</a>
 */
public class InstructionStoreArrayShort extends Instruction implements ArrayStore {
	public static int[] getOpcodes( ) {
		return new int[] {0x56};
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionStoreArrayShort load(int opcode, DataInputStream din) throws IOException {
			return new InstructionStoreArrayShort();
		}
	}
}
