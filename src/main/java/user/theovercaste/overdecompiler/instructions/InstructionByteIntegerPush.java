package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetConstant;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetConstant.ConstantType;

public class InstructionByteIntegerPush extends Instruction {
	private final int byteValue;

	public InstructionByteIntegerPush(int opcode, int byteValue) {
		super(opcode);
		this.byteValue = byteValue;
	}

	@Override
	public boolean isAction( ) {
		return true;
	}

	@Override
	public MethodAction getAction(ClassData originClass, Stack<MethodAction> stack) throws InstructionParsingException {
		return new MethodActionGetConstant(String.valueOf(byteValue), ConstantType.INT);
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x10};
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionByteIntegerPush load(int opcode, DataInputStream din) throws IOException {
			int b = din.readByte();
			return new InstructionByteIntegerPush(opcode, b);
		}
	}
}
