package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.EndOfStackException;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidStackTypeException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionAdd;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetter;

public class InstructionAdd extends Instruction {
	public InstructionAdd(int opcode) {
		super(opcode);
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x60, 0x61, 0x62, 0x63}; // iadd, ladd, fadd, dadd
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
		MethodAction valueOne = stack.pop();
		MethodAction valueTwo = stack.pop();
		if (valueOne instanceof MethodActionGetter) {
			if (valueTwo instanceof MethodActionGetter) {
				return new MethodActionAdd(lineNumber, (MethodActionGetter) valueOne, (MethodActionGetter) valueTwo);
			} else {
				throw new InvalidStackTypeException(valueTwo);
			}
		} else {
			throw new InvalidStackTypeException(valueOne);
		}
	}

	@Override
	public int getByteSize( ) {
		return 0;
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionAdd load(int opcode, DataInputStream din) throws IOException {
			return new InstructionAdd(opcode);
		}
	}
}
