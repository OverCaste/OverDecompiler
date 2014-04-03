package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc">ldc</a>
 */
public class InstructionLoadConstant extends Instruction {
	private final int constantIndex;

	public InstructionLoadConstant(int nameIndex) {
		constantIndex = nameIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x12};
	}

	public ConstantPoolEntry getValue(ConstantPoolEntry[] constantPool) {
		return constantPool[constantIndex];
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadConstant load(int opcode, DataInputStream din) throws IOException {
			int nameIndex = din.readUnsignedByte();
			return new InstructionLoadConstant(nameIndex);
		}
	}
}
