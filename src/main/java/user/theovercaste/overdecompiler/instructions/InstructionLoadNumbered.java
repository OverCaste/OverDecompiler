package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionLoadVariable;

public class InstructionLoadNumbered extends Instruction {
	public InstructionLoadNumbered(int opcode) {
		super(opcode);
	}

	public int getNumber( ) {
		return (opcode - 0x1a) & 3; // For clarity, this is (roughly) equivalent to (getOpcodes( )[0]) % 4, which takes advantage of the fact that all of the xload_n operations are sequential.
	}

	@Override
	public boolean isAction( ) {
		return true;
	}

	@Override
	public MethodAction getAction(int lineNumber, ClassData originClass, Stack<MethodAction> stack) throws InstructionParsingException {
		return new MethodActionLoadVariable(lineNumber, getNumber());
	}

	@Override
	public int getByteSize( ) {
		return 0;
	}

	public static int[] getOpcodes( ) {
		return new int[] {
				0x1a, 0x1b, 0x1c, 0x1d, // iload
				0x1e, 0x1f, 0x20, 0x21, // lload
				0x22, 0x23, 0x24, 0x25, // fload
				0x26, 0x27, 0x28, 0x29, // dload
				0x2a, 0x2b, 0x2c, 0x2d // aload
		};
	}

	public static Factory factory( ) {
		return new Factory();
	}

	public static class Factory extends Instruction.Factory {
		@Override
		public InstructionLoadNumbered load(int opcode, DataInputStream din) throws IOException {
			return new InstructionLoadNumbered(opcode);
		}
	}
}
