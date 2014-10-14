package user.theovercaste.overdecompiler.instructions;

import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

/**
 * An action that changes the stack without having a MethodAction associated with it.
 *
 */
public abstract class AbstractInstructionStackModifier extends Instruction {
    public AbstractInstructionStackModifier(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public AbstractInstructionStackModifier(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public final boolean isAction( ) {
        return false;
    }

    @Override
    public final MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getByteSize( ) {
        return 1;
    }
}