package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public class InstructionDup extends AbstractInstructionStackModifier {
    public InstructionDup(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionDup(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x59};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionDup load(int opcode, DataInputStream din) throws IOException {
            return new InstructionDup(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }

    @Override
    public void modifyStack(Stack<MethodMember> stack) {
        stack.push(stack.peek());
    }
}
