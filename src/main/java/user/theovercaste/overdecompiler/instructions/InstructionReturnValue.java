package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.parserdata.methodmembers.*;

public class InstructionReturnValue extends AbstractInstructionDirectAction {
    public InstructionReturnValue(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionReturnValue(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        if (stack.isEmpty()) {
            throw new EndOfStackException("There was no value to be returned!");
        }
        MethodMember value = stack.pop();
        if (value instanceof MethodActionGetter) {
            return new MethodActionReturnValue((MethodActionGetter) value);
        } else {
            throw new InvalidStackTypeException(value);
        }
    }

    @Override
    public int getByteSize( ) {
        return 0;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xAC, 0xAD, 0xAE, 0xAF, 0xB0}; // ireturn, lreturn, freturn, dreturn, areturn
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionReturnValue load(int opcode, DataInputStream din) throws IOException {
            return new InstructionReturnValue(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }
}
