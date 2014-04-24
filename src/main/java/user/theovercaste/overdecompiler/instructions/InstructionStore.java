package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.EndOfStackException;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidStackTypeException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetter;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionSetVariable;

public class InstructionStore extends Instruction {
	private final int value;

	public InstructionStore(int opcode, int value) {
		super(opcode);
		this.value = value;
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x36, 0x37, 0x38, 0x39, 0x3a};
	}

	public int getNumber( ) {
		return value;
	}

	@Override
	public boolean isAction( ) {
		return true;
	}

	@Override
	public MethodAction getAction(int lineNumber, ClassData originClass, Stack<MethodAction> stack) throws InstructionParsingException {
		if (stack.isEmpty()) {
			throw new EndOfStackException();
		}
		MethodAction a = stack.pop();
		if (a instanceof MethodActionGetter) {
			return new MethodActionSetVariable(lineNumber, getNumber(), (MethodActionGetter) a);
		} else {
			throw new InvalidStackTypeException(a);
		}
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
		public InstructionStore load(int opcode, DataInputStream din) throws IOException {
			int value = din.readUnsignedByte();
			return new InstructionStore(opcode, value);
		}
	}
}
