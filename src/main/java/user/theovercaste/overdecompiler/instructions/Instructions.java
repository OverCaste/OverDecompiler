package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import user.theovercaste.overdecompiler.exceptions.InvalidInstructionException;

public class Instructions {
	private static final HashMap<Integer, Instruction.Factory> factoryMap = new HashMap<>();

	static {
		loadInstruction(InstructionGetStatic.getOpcodes(), InstructionGetStatic.factory()); // This may be easier with reflection, but it would make the code brittle.
		loadInstruction(InstructionInvokeVirtual.getOpcodes(), InstructionInvokeVirtual.factory());
		loadInstruction(InstructionLoadConstant.getOpcodes(), InstructionLoadConstant.factory());
		loadInstruction(InstructionReturn.getOpcodes(), InstructionReturn.factory());
	}

	private static void loadInstruction(int[] hashCodes, Instruction.Factory b) {
		for (int i : hashCodes) {
			factoryMap.put(i, b);
		}
	}

	public static Instruction loadInstruction(int id, DataInputStream din) throws IOException {
		if (factoryMap.containsKey(id)) {
			return factoryMap.get(id).load(din);
		}
		throw new InvalidInstructionException("Instruction " + id + " (" + Integer.toHexString(id) + ") isn't defined.");
	}
}
