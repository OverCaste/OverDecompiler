package user.theovercaste.overdecompiler.instructions;

import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

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
    public void modifyStack(Stack<MethodMember> stack) {
        //Do nothing
    }

    @Override
    public final boolean isAction( ) {
        return true;
    }

    @Override
    public abstract MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException;
}
