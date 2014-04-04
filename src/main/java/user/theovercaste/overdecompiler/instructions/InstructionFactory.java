package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import user.theovercaste.overdecompiler.exceptions.InvalidInstructionException;

public class InstructionFactory {
	private static final HashMap<Integer, Instruction.Factory> factoryMap = new HashMap<>();

	static {
		loadInstruction(InstructionGetStatic.getOpcodes(), InstructionGetStatic.factory()); // This may be easier with reflection, but it would make the code brittle.
		loadInstruction(InstructionInvokeVirtual.getOpcodes(), InstructionInvokeVirtual.factory());
		loadInstruction(InstructionLoadConstant.getOpcodes(), InstructionLoadConstant.factory());

		// Load instructions: These push values from variables onto the stack.
		loadInstruction(InstructionLoadDouble.getOpcodes(), InstructionLoadReference.factory());
		loadInstruction(InstructionLoadDoubleNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());
		loadInstruction(InstructionLoadFloat.getOpcodes(), InstructionLoadReference.factory());
		loadInstruction(InstructionLoadFloatNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());
		loadInstruction(InstructionLoadInteger.getOpcodes(), InstructionLoadReference.factory());
		loadInstruction(InstructionLoadIntegerNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());
		loadInstruction(InstructionLoadLong.getOpcodes(), InstructionLoadReference.factory());
		loadInstruction(InstructionLoadLongNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());
		loadInstruction(InstructionLoadReference.getOpcodes(), InstructionLoadReference.factory());
		loadInstruction(InstructionLoadReferenceNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());

		// Array load instructions: These push values from an array at the specified index onto the stack.
		loadInstruction(InstructionLoadArrayValue.getOpcodes(), InstructionLoadArrayValue.factory());
		loadInstruction(InstructionLoadArrayChar.getOpcodes(), InstructionLoadArrayChar.factory());
		loadInstruction(InstructionLoadArrayDouble.getOpcodes(), InstructionLoadArrayDouble.factory());
		loadInstruction(InstructionLoadArrayFloat.getOpcodes(), InstructionLoadArrayFloat.factory());
		loadInstruction(InstructionLoadArrayInteger.getOpcodes(), InstructionLoadArrayInteger.factory());
		loadInstruction(InstructionLoadArrayLong.getOpcodes(), InstructionLoadArrayLong.factory());
		loadInstruction(InstructionLoadArrayObject.getOpcodes(), InstructionLoadArrayObject.factory());
		loadInstruction(InstructionLoadArrayShort.getOpcodes(), InstructionLoadArrayShort.factory());

		// Array store instructions: These pull a value from the stack into the specified index of an array.
		loadInstruction(InstructionStoreArrayByte.getOpcodes(), InstructionStoreArrayByte.factory());
		loadInstruction(InstructionStoreArrayChar.getOpcodes(), InstructionStoreArrayChar.factory());
		loadInstruction(InstructionStoreArrayDouble.getOpcodes(), InstructionStoreArrayDouble.factory());
		loadInstruction(InstructionStoreArrayFloat.getOpcodes(), InstructionStoreArrayFloat.factory());
		loadInstruction(InstructionStoreArrayInteger.getOpcodes(), InstructionStoreArrayInteger.factory());
		loadInstruction(InstructionStoreArrayLong.getOpcodes(), InstructionStoreArrayLong.factory());
		loadInstruction(InstructionStoreArrayObject.getOpcodes(), InstructionStoreArrayObject.factory());
		loadInstruction(InstructionStoreArrayShort.getOpcodes(), InstructionStoreArrayShort.factory());

		// Return instructions: These are the bytecode equivalents to the 'return' statement.
		loadInstruction(InstructionReturnVoid.getOpcodes(), InstructionReturnVoid.factory());
		loadInstruction(InstructionReturnFloat.getOpcodes(), InstructionReturnFloat.factory());
		loadInstruction(InstructionReturnDouble.getOpcodes(), InstructionReturnDouble.factory());
		loadInstruction(InstructionReturnInteger.getOpcodes(), InstructionReturnInteger.factory());
		loadInstruction(InstructionReturnLong.getOpcodes(), InstructionReturnLong.factory());
		loadInstruction(InstructionReturnObject.getOpcodes(), InstructionReturnObject.factory());

	}

	private static void loadInstruction(int[] opcodes, Instruction.Factory b) {
		for (int i : opcodes) {
			if (factoryMap.containsKey(i)) {
				throw new RuntimeException("The opcode " + i + " (0x" + Integer.toHexString(i) + ") is already used by instruction " + factoryMap.get(i).getClass());
			}
			factoryMap.put(i, b);
		}
	}

	public static Instruction loadInstruction(int id, DataInputStream din) throws IOException {
		if (factoryMap.containsKey(id)) {
			return factoryMap.get(id).load(id, din);
		}
		throw new InvalidInstructionException("Instruction " + id + " (" + Integer.toHexString(id) + ") isn't defined.");
	}
}
