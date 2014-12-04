package user.theovercaste.overdecompiler.instructions;

import java.util.Stack;

import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;

/**
 * An Instruction that directly translates to a MethodAction via {@link #getAction(ClassData, Stack)}
 */
public abstract class AbstractInstructionDirectAction extends Instruction {
    public AbstractInstructionDirectAction(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public AbstractInstructionDirectAction(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public void modifyStack(MethodDecompileContext ctx) {
        // Do nothing
    }

    @Override
    public final boolean isAction( ) {
        return true;
    }

    @Override
    public abstract MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException;
}
