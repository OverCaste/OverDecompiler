package user.theovercaste.overdecompiler.parsers.methodblockparsers;

import java.util.*;

import user.theovercaste.overdecompiler.codeinternals.ArithmeticComparison;
import user.theovercaste.overdecompiler.instructions.AbstractInstructionComparison;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.parserdata.method.*;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodBlockContainer;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodBlockContainer.Member;

public class MethodBlockParserIf implements MethodBlockParser {
    /** The byte at which the currently scanned jump instruction ends at. */
    private int scanEnd = -1;
    private ArithmeticComparison operator;
    private ScanState state;
    private boolean scanning = false;

    @Override
    public void parse(ListIterator<MethodBlockContainer.Member> listIterator) {
        state = ScanState.NO_MATCH;
        if (!listIterator.hasNext()) {
            return;
        }
        MethodBlockContainer.Member member = listIterator.next();
        Instruction instruction = member.getInstruction();
        if (scanning && instruction.getByteIndex() >= scanEnd) {
            state = ScanState.SCAN_ENDED;
        }
        if (instruction instanceof AbstractInstructionComparison) {
            int branchIndex = ((AbstractInstructionComparison) instruction).getBranchIndex();
            operator = ((AbstractInstructionComparison) instruction).getComparisonOperator();
            if (branchIndex > instruction.getByteIndex()) { // If statements go forwards, otherwise it's a do while
                scanEnd = branchIndex;
                state = ScanState.SCAN_STARTED;
                scanning = true;
            }
        }
    }
    
    @Override
    public MethodBlockContainer createContainer(List<Member> instructions) {
        if (operator == null) {
            throw new IllegalStateException("Tried to create a container when the operator wasn't defined.");
        }
        return new IfContainer(instructions, operator);
    }

    @Override
    public void reset( ) {
        scanEnd = -1;
        operator = null;
        state = null;
        scanning = false;
    }

    private static class IfContainer extends MethodBlockContainer {
        private final ArithmeticComparison operator;

        public IfContainer(List<Member> members, ArithmeticComparison operator) {
            super(members);
            this.operator = operator;
        }

        @Override
        public MethodBlock toMethodBlock(List<MethodMember> members, Stack<MethodMember> parentMemberStack) {
            MethodActionGetter right = (MethodActionGetter) parentMemberStack.pop();
            MethodActionGetter left = (MethodActionGetter) parentMemberStack.pop();
            MethodBlock ret = new MethodBlockIf(new MethodActionComparison(left, operator, right));
            for (MethodMember m : members) {
                ret.addMember(m);
            }
            return ret;
        }
    }

    @Override
    public ScanState getState( ) {
        if(state == null) {
            throw new IllegalStateException("Attempted to get the state of a MethodBlockParser before its parse method was called!");
        }
        return state;
    }
}
