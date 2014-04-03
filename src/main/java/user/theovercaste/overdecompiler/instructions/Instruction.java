package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;

import user.theovercaste.overdecompiler.instructiontypes.InstructionType;

public abstract class Instruction {
	public abstract Collection<InstructionType> getInstructionTypes(int opcode);

	public abstract Collection<Integer> getInstructionOpcodes(InstructionType t);

	public static abstract class Factory {
		public abstract Instruction load(int opcode, DataInputStream din) throws IOException;
	}
}
