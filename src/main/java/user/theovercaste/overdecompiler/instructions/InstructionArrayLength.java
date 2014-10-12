package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.parserdata.method.*;

public class InstructionArrayLength extends AbstractInstructionDirectAction {
    public InstructionArrayLength(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionArrayLength(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        if(stack.isEmpty()) {
            throw new EndOfStackException("There was no array to get the length of!");
        }
        MethodMember value = stack.pop();
        if (value instanceof MethodActionGetter) {
            return new MethodActionGetArrayLength((MethodActionGetter) value);
        } else {
            throw new InvalidStackTypeException(value);
        }
    }

    @Override
    public int getByteSize( ) {
        return 0;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xBE};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionArrayLength load(int opcode, DataInputStream din) throws IOException {
            return new InstructionArrayLength(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }
}
