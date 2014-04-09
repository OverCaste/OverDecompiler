package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryFieldReference;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetStaticField;

public class InstructionGetStatic extends Instruction {
	private final int nameIndex;

	public InstructionGetStatic(int opcode, int nameIndex) {
		super(opcode);
		this.nameIndex = nameIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0xb2};
	}

	public ConstantPoolEntryFieldReference getField(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(nameIndex, constantPool.length);
		ConstantPoolEntry e = constantPool[nameIndex];
		if (e instanceof ConstantPoolEntryFieldReference) {
			return (ConstantPoolEntryFieldReference) e;
		}
		throw PoolPreconditions.getInvalidType(constantPool, nameIndex);
	}

	@Override
	public boolean isAction( ) {
		return true;
	}

	@Override
	public MethodAction getAction(ClassData originClass, Stack<Instruction> stack) throws InstructionParsingException {
		ConstantPoolEntryFieldReference f;
		try {
			f = getField(originClass.getConstantPool());
			return new MethodActionGetStaticField(f.getName(originClass.getConstantPool()), new ClassPath(f.getClassName(originClass.getConstantPool()).replace("/", ".")));
		} catch (InvalidConstantPoolPointerException e) {
			throw new InstructionParsingException(e);
		}
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionGetStatic load(int opcode, DataInputStream din) throws IOException {
			int nameIndex = din.readUnsignedShort();
			return new InstructionGetStatic(opcode, nameIndex);
		}
	}
}
