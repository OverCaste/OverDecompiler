package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryFloat;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryInteger;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryString;
import user.theovercaste.overdecompiler.datahandlers.ImportList;

public class InstructionLoadConstant extends Instruction {
	private final int constantIndex;

	public InstructionLoadConstant(int nameIndex) {
		this.constantIndex = nameIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x12};
	}

	@Override
	public String toJava(ImportList imports, ConstantPoolEntry[] constantPool, Stack<Instruction> stack) {
		ConstantPoolEntry e = getValue(constantPool);
		if (e instanceof ConstantPoolEntryInteger) {
			return "\"Unknown - LDC constant integer?\"";
			// return String.valueOf(((ConstantPoolEntryInteger)e).getValue());
		}
		if (e instanceof ConstantPoolEntryFloat) {
			return "\"Unknown - LDC constant float?\"";
		}
		if (e instanceof ConstantPoolEntryString) {
			return "\"" + ((ConstantPoolEntryString) e).getValue(constantPool) + "\"";
		}

		return null;
	}

	public ConstantPoolEntry getValue(ConstantPoolEntry[] constantPool) {
		return constantPool[this.constantIndex];
	}

	@Override
	public boolean printable( ) {
		return false;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadConstant load(DataInputStream din) throws IOException {
			int nameIndex = din.readUnsignedByte();
			return new InstructionLoadConstant(nameIndex);
		}
	}
}
