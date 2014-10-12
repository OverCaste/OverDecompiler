package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

public class InstructionNew extends AbstractInstructionDummy {
    private final int newIndex;

    public InstructionNew(int opcode, int byteIndex, int instructionIndex, int lineNumber, int newIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.newIndex = newIndex;
    }

    public InstructionNew(int opcode, int byteIndex, int instructionIndex, int byteValue) {
        super(opcode, byteIndex, instructionIndex);
        this.newIndex = byteValue;
    }
    
    public int getNewIndex( ) {
        return newIndex;
    }

    @Override
    public int getByteSize( ) {
        return 2;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xBB};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionNew load(int opcode, DataInputStream din) throws IOException {
            int newIndex = din.readUnsignedShort();
            return new InstructionNew(opcode, byteIndex, instructionIndex, lineNumber, newIndex);
        }
    }
}
