package user.theovercaste.overdecompiler.instructions.comparisons;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.codeinternals.ArithmeticComparison;
import user.theovercaste.overdecompiler.instructions.AbstractInstructionComparison;
import user.theovercaste.overdecompiler.instructions.Instruction;

public class InstructionIfNotEqual extends AbstractInstructionComparison {
    public InstructionIfNotEqual(int opcode, int byteIndex, int instructionIndex, int lineNumber, int branchIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber, branchIndex);
    }

    public InstructionIfNotEqual(int opcode, int byteIndex, int instructionIndex, int branchIndex) {
        super(opcode, byteIndex, instructionIndex, branchIndex);
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xA0, 0xA6}; // integer comparison, reference comparison
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionIfNotEqual load(int opcode, DataInputStream din) throws IOException {
            int branchIndex = din.readShort();
            return new InstructionIfNotEqual(opcode, byteIndex, instructionIndex, lineNumber, branchIndex);
        }
    }

    @Override
    public ArithmeticComparison getComparisonOperator( ) {
        return ArithmeticComparison.EQUAL_TO; // Since we skip if not, then we continue if it IS, making this the opposite.
    }
}
