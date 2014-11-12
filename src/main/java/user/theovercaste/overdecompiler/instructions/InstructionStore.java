package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.parserdata.methodmembers.*;

public class InstructionStore extends AbstractInstructionDirectAction {
    private final int value;

    public InstructionStore(int opcode, int byteIndex, int instructionIndex, int lineNumber, int value) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.value = value;
    }

    public InstructionStore(int opcode, int byteIndex, int instructionIndex, int value) {
        super(opcode, byteIndex, instructionIndex);
        this.value = value;
    }

    public int getNumber( ) {
        return value;
    }

    @Override
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        if (stack.isEmpty()) {
            throw new EndOfStackException();
        }
        MethodMember a = stack.pop();
        if (a instanceof MethodActionGetter) {
            return new MethodActionSetVariable(getNumber(), (MethodActionGetter) a);
        } else {
            throw new InvalidStackTypeException(a);
        }
    }

    @Override
    public int getByteSize( ) {
        return 1;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x36, 0x37, 0x38, 0x39, 0x3a};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionStore load(int opcode, DataInputStream din) throws IOException {
            int value = din.readUnsignedByte();
            return new InstructionStore(opcode, byteIndex, instructionIndex, lineNumber, value);
        }
    }
}
