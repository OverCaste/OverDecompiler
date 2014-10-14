package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.instructions.AbstractInstructionComparison;

public abstract class DummyMethodMemberJump extends MethodMember {
    private final AbstractInstructionComparison jumpInstruction;
    
    public DummyMethodMemberJump(AbstractInstructionComparison jumpInstruction) {
        super(Type.UNPRINTABLE);
        this.jumpInstruction = jumpInstruction;
    }

    public AbstractInstructionComparison getJumpInstruction( ) {
        return jumpInstruction;
    }
}
