package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryMethodReference;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class InstructionInvokeVirtual extends Instruction {
	private final int methodIndex;

	public InstructionInvokeVirtual(int methodIndex) {
		this.methodIndex = methodIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0xb6};
	}

	public ConstantPoolEntryMethodReference getMethod(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		ConstantPoolEntry e = constantPool[methodIndex];
		if (e instanceof ConstantPoolEntryMethodReference) {
			return (ConstantPoolEntryMethodReference) e;
		}
		throw new InvalidConstantPoolPointerException("Invoke virtual has an invalid reference: " + methodIndex + ".");
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionInvokeVirtual load(int opcode, DataInputStream din) throws IOException {
			int methodIndex = din.readUnsignedShort();
			return new InstructionInvokeVirtual(methodIndex);
		}
	}
}
