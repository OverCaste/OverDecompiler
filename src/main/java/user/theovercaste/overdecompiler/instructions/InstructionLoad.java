package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionLoadVariable;

public class InstructionLoad extends Instruction {
	private final int referenceIndex;

	public InstructionLoad(int opcode, int referenceIndex) {
		super(opcode);
		this.referenceIndex = referenceIndex;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x15, 0x16, 0x17, 0x18, 0x19};
	}

	public int getNumber( ) {
		return referenceIndex;
	}

	@Override
	public boolean isAction( ) {
		return true;
	}

	@Override
	public MethodAction getAction(int lineNumber, ClassData originClass, Stack<MethodAction> stack) throws InstructionParsingException {
		return new MethodActionLoadVariable(lineNumber, referenceIndex);
	}

	@Override
	public int getByteSize( ) {
		return 1;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoad load(int opcode, DataInputStream din) throws IOException {
			int referenceIndex = din.readUnsignedByte();
			return new InstructionLoad(opcode, referenceIndex);
		}
	}
}
