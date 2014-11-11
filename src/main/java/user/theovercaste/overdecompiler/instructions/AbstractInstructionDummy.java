package user.theovercaste.overdecompiler.instructions;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodDecompileContext;

public abstract class AbstractInstructionDummy extends Instruction {
    public AbstractInstructionDummy(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public AbstractInstructionDummy(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public boolean isAction( ) {
        return false;
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        throw new UnsupportedOperationException("Dummy classes don't have associated actions!");
    }

    @Override
    public void modifyStack(MethodDecompileContext ctx) {
        // Do nothing
    }
}
