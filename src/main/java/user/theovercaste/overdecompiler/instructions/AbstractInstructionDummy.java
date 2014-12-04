package user.theovercaste.overdecompiler.instructions;

import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;

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
