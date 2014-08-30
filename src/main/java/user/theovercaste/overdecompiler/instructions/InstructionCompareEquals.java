package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

public class InstructionCompareEquals extends AbstractInstructionComparator {
    public InstructionCompareEquals(int opcode, int byteIndex, int instructionIndex, int lineNumber, int branchIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber, branchIndex);
    }

    public InstructionCompareEquals(int opcode, int byteIndex, int instructionIndex, int branchIndex) {
        super(opcode, byteIndex, instructionIndex, branchIndex);
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xa5, 0x9f, 0x99}; // if_acmpeq (references equal), if_icmpeq (integers equal), ifeq (value is 0)
    }

    public int getNumber( ) {
        return branchIndex;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionCompareEquals load(int opcode, DataInputStream din) throws IOException {
            int branchIndex = din.readShort();
            return new InstructionCompareEquals(opcode, byteIndex, instructionIndex, lineNumber, branchIndex);
        }
    }
}
