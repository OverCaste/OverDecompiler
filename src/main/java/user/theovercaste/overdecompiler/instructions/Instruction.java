package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;

public abstract class Instruction {
	protected final int opcode;

	protected Instruction(int opcode) {
		this.opcode = opcode;
	}

	public abstract boolean isAction( );

	public abstract MethodAction getAction(ClassData originClass, Stack<MethodAction> stack) throws InstructionParsingException;

	public int getOpcode( ) {
		return opcode;
	}

	public static abstract class Factory {
		public abstract Instruction load(int opcode, DataInputStream din) throws IOException;
	}
}
