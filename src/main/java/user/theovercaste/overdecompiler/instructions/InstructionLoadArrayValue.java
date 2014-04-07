package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;

import user.theovercaste.overdecompiler.instructiontypes.ArrayLoad;
import user.theovercaste.overdecompiler.instructiontypes.InstructionType;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.baload">baload</a>
 */
public class InstructionLoadArrayValue extends Instruction implements ArrayLoad {
	public static int[] getOpcodes( ) {
		return new int[] {0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35};
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadArrayValue load(int opcode, DataInputStream din) throws IOException {
			return new InstructionLoadArrayValue();
		}
	}

	@Override
	public Collection<InstructionType> getInstructionTypes(int opcode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Integer> getInstructionOpcodes(InstructionType t) {
		// TODO Auto-generated method stub
		return null;
	}
}
