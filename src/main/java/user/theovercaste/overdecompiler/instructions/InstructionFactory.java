package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.HashMap;

import user.theovercaste.overdecompiler.exceptions.InvalidInstructionException;

public class InstructionFactory {
	private static final HashMap<Integer, Instruction.Factory> factoryMap = new HashMap<>();

	static {
		registerInstruction(InstructionGetStatic.getOpcodes(), InstructionGetStatic.factory()); // This may be easier with reflection, but it would make the code brittle.
		registerInstruction(InstructionInvokeVirtual.getOpcodes(), InstructionInvokeVirtual.factory());
		registerInstruction(InstructionLoadConstant.getOpcodes(), InstructionLoadConstant.factory());

		// Load instructions: These push values from variables onto the stack.
		registerInstruction(InstructionLoadDouble.getOpcodes(), InstructionLoadReference.factory());
		registerInstruction(InstructionLoadDoubleNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());
		registerInstruction(InstructionLoadFloat.getOpcodes(), InstructionLoadReference.factory());
		registerInstruction(InstructionLoadFloatNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());
		registerInstruction(InstructionLoadInteger.getOpcodes(), InstructionLoadReference.factory());
		registerInstruction(InstructionLoadIntegerNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());
		registerInstruction(InstructionLoadLong.getOpcodes(), InstructionLoadReference.factory());
		registerInstruction(InstructionLoadLongNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());
		registerInstruction(InstructionLoadReference.getOpcodes(), InstructionLoadReference.factory());
		registerInstruction(InstructionLoadReferenceNumbered.getOpcodes(), InstructionLoadReferenceNumbered.factory());

		// Array load instructions: These push values from an array at the specified index onto the stack.
		registerInstruction(InstructionLoadArrayValue.getOpcodes(), InstructionLoadArrayValue.factory());

		// Array store instructions: These pull a value from the stack into the specified index of an array.
		registerInstruction(InstructionStoreArrayByte.getOpcodes(), InstructionStoreArrayByte.factory());
		registerInstruction(InstructionStoreArrayChar.getOpcodes(), InstructionStoreArrayChar.factory());
		registerInstruction(InstructionStoreArrayDouble.getOpcodes(), InstructionStoreArrayDouble.factory());
		registerInstruction(InstructionStoreArrayFloat.getOpcodes(), InstructionStoreArrayFloat.factory());
		registerInstruction(InstructionStoreArrayInteger.getOpcodes(), InstructionStoreArrayInteger.factory());
		registerInstruction(InstructionStoreArrayLong.getOpcodes(), InstructionStoreArrayLong.factory());
		registerInstruction(InstructionStoreArrayObject.getOpcodes(), InstructionStoreArrayObject.factory());
		registerInstruction(InstructionStoreArrayShort.getOpcodes(), InstructionStoreArrayShort.factory());

		// Return instructions: These are the bytecode equivalents to the 'return' statement.
		registerInstruction(InstructionReturnVoid.getOpcodes(), InstructionReturnVoid.factory());
		registerInstruction(InstructionReturnFloat.getOpcodes(), InstructionReturnFloat.factory());
		registerInstruction(InstructionReturnDouble.getOpcodes(), InstructionReturnDouble.factory());
		registerInstruction(InstructionReturnInteger.getOpcodes(), InstructionReturnInteger.factory());
		registerInstruction(InstructionReturnLong.getOpcodes(), InstructionReturnLong.factory());
		registerInstruction(InstructionReturnObject.getOpcodes(), InstructionReturnObject.factory());

	}

	private static void registerInstruction(int[] opcodes, Instruction.Factory b) {
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

	public static Instruction loadInstruction(DataInputStream din) throws IOException {
		int id = din.readUnsignedByte();
		if (id < 0) {
			throw new EOFException();
		}
		return loadInstruction(id, din);
	}
}
