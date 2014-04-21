package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryClass;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryFloat;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryInteger;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryString;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolValueRetriever;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetConstant;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetConstant.ConstantType;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc">ldc</a>
 */
public class InstructionLoadConstant extends Instruction {
	private final int constantIndex;

	public InstructionLoadConstant(int opcode, int nameIndex) {
		super(opcode);
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

	@Override
	public boolean isAction( ) {
		return true;
	}

	@Override
	public MethodAction getAction(ClassData originClass, Stack<MethodAction> stack) throws InstructionParsingException {
		ConstantPoolEntry e = getValue(originClass.getConstantPool());
		if (e instanceof ConstantPoolEntryInteger) {
			return new MethodActionGetConstant(String.valueOf(((ConstantPoolEntryInteger) e).getValue()), ConstantType.INT);
		}
		else if (e instanceof ConstantPoolEntryFloat) {
			return new MethodActionGetConstant(String.valueOf(((ConstantPoolEntryFloat) e).getValue()), ConstantType.FLOAT);
		}
		else if (e instanceof ConstantPoolEntryString) {
			try {
				return new MethodActionGetConstant(ConstantPoolValueRetriever.getInstance().getString(originClass.getConstantPool(), constantIndex), ConstantType.STRING);
			} catch (InvalidConstantPoolPointerException ex) {
				// Do nothing, fail through
			}
		}
		else if (e instanceof ConstantPoolEntryClass) {
			try {
				throw new InstructionParsingException("Parsing class LDC hasn't been implemented yet. Forward this to the developer: " + ((ConstantPoolEntryClass) e).getName(originClass.getConstantPool()));
			} catch (InvalidConstantPoolPointerException ex) {
				ex.printStackTrace();
			}
		}
		throw new InstructionParsingException(("Invalid type for LDC: " + e) == null ? "null" : e.getClass().getName());
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadConstant load(int opcode, DataInputStream din) throws IOException {
			int nameIndex = din.readUnsignedByte();
			return new InstructionLoadConstant(opcode, nameIndex);
		}
	}
}
