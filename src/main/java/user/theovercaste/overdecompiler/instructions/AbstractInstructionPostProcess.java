package user.theovercaste.overdecompiler.instructions;

/**
 * These instructions are processed afterwards, for things like control structures.
 */
public abstract class AbstractInstructionPostProcess extends AbstractInstructionDummy {
    public AbstractInstructionPostProcess(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public AbstractInstructionPostProcess(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }
}
