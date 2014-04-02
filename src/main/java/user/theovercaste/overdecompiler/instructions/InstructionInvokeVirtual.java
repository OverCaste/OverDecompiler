package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.DecompileConstants;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryMethodReference;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryNameAndType;
import user.theovercaste.overdecompiler.datahandlers.ImportList;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class InstructionInvokeVirtual extends Instruction {
	private final int methodIndex;

	public InstructionInvokeVirtual(int methodIndex) {
		this.methodIndex = methodIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0xb6};
	}

	public String toJava(ImportList imports, ConstantPoolEntry[] constantPool, Stack<Instruction> stack) {
		ConstantPoolEntryNameAndType methodEntry;
		try {
			methodEntry = (ConstantPoolEntryNameAndType) constantPool[getMethod(constantPool).getNameAndTypeIndex()];
		} catch (InvalidConstantPoolPointerException ex) {
			ex.printStackTrace();
			return DecompileConstants.ERROR_INVALID_DATA;
		}

		// ConstantPoolEntryClass otherEntry = (ConstantPoolEntryClass) constantPool[getMethod(constantPool).getClassIndex()];
		// System.out.println("Other: " + otherEntry.getName(constantPool)); ex: PrintStream

		String method;
		try {
			method = methodEntry.getName(constantPool);
		} catch (InvalidConstantPoolPointerException ex) {
			method = DecompileConstants.ERROR_INVALID_DATA;
			ex.printStackTrace();
		}
		String type;
		try {
			type = methodEntry.getDescription(constantPool);
		} catch (InvalidConstantPoolPointerException ex) {
			type = DecompileConstants.ERROR_INVALID_DATA;
			ex.printStackTrace();
		}

		Instruction iValue = stack.pop();
		// String value = iValue.toJava(imports, constantPool, stack);
		Instruction iInvoker = stack.pop();

		System.out.println("Invoking " + iInvoker.toJava(imports, constantPool, stack) + "." + method + "(" + iValue.toJava(imports, constantPool, stack) + ")");

		// ConstantPoolEntryClass classEntry = (ConstantPoolEntryClass) constantPool[iGetStatic.getField(constantPool).getClassIndex()];

		// String object;
		// try {
		// object = iGetStatic.getField(constantPool).getName(constantPool);
		// } catch (InvalidConstantPoolPointerException e) {
		// object = DecompileConstants.ERROR_INVALID_DATA;
		// e.printStackTrace();
		// }
		// System.out.println("Invoking " + classEntry.getName(constantPool) + "." + object + "." + method + "(" + value + ");");
		System.out.println("Type: " + type);
		return "";
	}

	public ConstantPoolEntryMethodReference getMethod(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		ConstantPoolEntry e = constantPool[methodIndex];
		if (e instanceof ConstantPoolEntryMethodReference) {
			return (ConstantPoolEntryMethodReference) e;
		}
		throw new InvalidConstantPoolPointerException("Invoke virtual has an invalid reference: " + methodIndex + ".");
	}

	public boolean printable( ) {
		return true;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionInvokeVirtual load(DataInputStream din) throws IOException {
			int methodIndex = din.readUnsignedShort();
			return new InstructionInvokeVirtual(methodIndex);
		}
	}
}
