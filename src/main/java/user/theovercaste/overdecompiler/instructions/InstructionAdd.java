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
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public class InstructionAdd extends Instruction {
    public InstructionAdd(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionAdd(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public boolean isAction( ) {
        return true;
    }

    @Override
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        if (stack.isEmpty()) {
            throw new EndOfStackException();
        }
        MethodMember valueOne = stack.pop();
        MethodMember valueTwo = stack.pop();
        if (valueOne instanceof MethodActionGetter) {
            if (valueTwo instanceof MethodActionGetter) {
                return new MethodActionAdd((MethodActionGetter) valueOne, (MethodActionGetter) valueTwo);
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

    public static int[] getOpcodes( ) {
        return new int[] {0x60, 0x61, 0x62, 0x63}; // iadd, ladd, fadd, dadd
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionAdd load(int opcode, DataInputStream din) throws IOException {
            return new InstructionAdd(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }
}
