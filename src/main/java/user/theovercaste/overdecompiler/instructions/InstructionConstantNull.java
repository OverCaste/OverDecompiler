package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetConstant;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetConstant.ConstantType;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aconst_null">aconst_null</a>
 */
public class InstructionConstantNull extends Instruction {
	public InstructionConstantNull(int opcode) {
		super(opcode);
	}

	public static int[] getOpcodes( ) {
		return new int[] {0x1};
	}

	@Override
	public boolean isAction( ) {
		return true;
	}

	@Override
	public MethodAction getAction(int lineNumber, ClassData originClass, Stack<MethodAction> stack) throws InstructionParsingException {
		return new MethodActionGetConstant(lineNumber, null, ConstantType.NULL);
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
		public InstructionConstantNull load(int opcode, DataInputStream din) throws IOException {
			return new InstructionConstantNull(opcode);
		}
	}
}
