package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.DecompileConstants;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryFieldReference;
import user.theovercaste.overdecompiler.datahandlers.ImportList;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;

public class InstructionGetStatic extends Instruction {
	private final int nameIndex;

	public InstructionGetStatic(int nameIndex) {
		this.nameIndex = nameIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0xb2};
	}

	public String toJava(ImportList imports, ConstantPoolEntry[] constantPool, Stack<Instruction> stack) {
		try {
			ConstantPoolEntryFieldReference f = getField(constantPool);
			imports.addQualifiedPath(f.getClassName(constantPool));
			return imports.getQualifiedName(f.getClassName(constantPool)) + "." + f.getName(constantPool);
		} catch (InvalidConstantPoolPointerException e) {
			e.printStackTrace();
		}
		return DecompileConstants.ERROR_INVALID_DATA;
	}

	public ConstantPoolEntryFieldReference getField(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(nameIndex, constantPool.length);
		ConstantPoolEntry e = constantPool[nameIndex];
		if (e instanceof ConstantPoolEntryFieldReference) {
			return (ConstantPoolEntryFieldReference) e;
		}
		throw PoolPreconditions.getInvalidType(constantPool, nameIndex);
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionGetStatic load(int opcode, DataInputStream din) throws IOException {
			int nameIndex = din.readUnsignedShort();
			return new InstructionGetStatic(nameIndex);
		}
	}
}
