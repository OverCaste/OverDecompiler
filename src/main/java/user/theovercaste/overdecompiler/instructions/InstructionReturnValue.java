package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidStackTypeException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetter;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionReturnValue;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public class InstructionReturnValue extends Instruction {
    public InstructionReturnValue(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionReturnValue(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public boolean isAction( ) {
        return true;
    }

    @Override
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
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
