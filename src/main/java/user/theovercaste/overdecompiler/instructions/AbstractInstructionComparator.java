package user.theovercaste.overdecompiler.instructions;

import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public abstract class AbstractInstructionComparator extends Instruction {
    protected final int branchIndex;

    public AbstractInstructionComparator(int opcode, int byteIndex, int instructionIndex, int lineNumber, int branchIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.branchIndex = branchIndex;
    }

    public AbstractInstructionComparator(int opcode, int byteIndex, int instructionIndex, int branchIndex) {
        super(opcode, byteIndex, instructionIndex);
        this.branchIndex = branchIndex;
    }

    @Override
    public boolean isAction( ) {
        return false;
    }

    @Override
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void modifyStack(Stack<MethodMember> stack) {
        //Do nothing
    }

    @Override
    public int getByteSize( ) {
        return 2;
    }
}
