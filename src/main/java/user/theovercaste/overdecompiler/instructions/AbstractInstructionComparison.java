package user.theovercaste.overdecompiler.instructions;

import java.util.Stack;

import user.theovercaste.overdecompiler.codeinternals.ArithmeticComparison;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public abstract class AbstractInstructionComparison extends Instruction {
    protected final int branchOffset;

    public AbstractInstructionComparison(int opcode, int byteIndex, int instructionIndex, int lineNumber, int branchOffset) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.branchOffset = branchOffset;
    }

    public AbstractInstructionComparison(int opcode, int byteIndex, int instructionIndex, int branchOffset) {
        super(opcode, byteIndex, instructionIndex);
        this.branchOffset = branchOffset;
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
        // Do nothing
    }

    @Override
    public int getByteSize( ) {
        return 2;
    }

    public abstract ArithmeticComparison getComparisonOperator( );

    public int getBranchIndex( ) {
        return branchOffset + byteIndex;
    }
}
