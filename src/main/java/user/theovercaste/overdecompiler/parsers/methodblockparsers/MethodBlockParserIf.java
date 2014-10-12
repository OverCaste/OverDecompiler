package user.theovercaste.overdecompiler.parsers.methodblockparsers;

import java.util.*;

import user.theovercaste.overdecompiler.codeinternals.ArithmeticComparison;
import user.theovercaste.overdecompiler.instructions.AbstractInstructionComparison;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.parserdata.method.*;
import user.theovercaste.overdecompiler.parsers.methodparsers.*;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodBlockContainer.Member;

public class MethodBlockParserIf implements MethodBlockParser {
    private int startIndex = -1;
    /** The byte at which the currently scanned jump instruction ends at. */
    private int scanEnd = -1;
    private ArithmeticComparison operator;
    
    @Override
    public ScanState parse(AbstractMethodParser subParser,  ListIterator<MethodBlockContainer.Member> listIterator) {
        int index = listIterator.nextIndex();
        if(!listIterator.hasNext()) {
            return ScanState.NO_MATCH;
        }
        MethodBlockContainer.Member member = listIterator.next();
        ScanState state = ScanState.NO_MATCH;
        Instruction instruction = member.getInstruction();
        if (startIndex >= 0) {
            if (instruction.getByteIndex() >= scanEnd) {
                System.out.println("Found an if statement between " + startIndex + " and " + index);
                state = ScanState.SCAN_ENDED;
            }
        }
        if (instruction instanceof AbstractInstructionComparison) {
            int branchIndex = ((AbstractInstructionComparison) instruction).getBranchIndex();
            operator = ((AbstractInstructionComparison)instruction).getComparisonOperator();
            if (branchIndex > instruction.getByteIndex()) { // If statements go forwards, otherwise it's a do while
                startIndex = index;
                scanEnd = branchIndex;
                state = ScanState.SCAN_STARTED;
            }
        }
        return state;
    }

    @Override
    public int getBlockHeaderCount( ) {
        return 2;
    }

    @Override
    public MethodBlockContainer createContainer(List<Member> instructions) {
        if(operator == null) {
            throw new IllegalStateException("Tried to create a container when the operator wasn't defined.");
        }
        return new IfContainer(instructions, operator);
    }

    @Override
    public void reset( ) {
        startIndex = -1;
        scanEnd = -1;
        operator = null;
    }
    
    private static class IfContainer extends MethodBlockContainer {
        private final ArithmeticComparison operator;
        
        public IfContainer(List<Member> members, ArithmeticComparison operator) {
            super(members);
            this.operator = operator;
        }

        @Override
        public MethodBlock toMethodBlock(List<MethodMember> members, Stack<MethodMember> parentMemberStack) {
            MethodActionGetter right = (MethodActionGetter)parentMemberStack.pop();
            MethodActionGetter left = (MethodActionGetter)parentMemberStack.pop();
            MethodBlock ret = new MethodBlockIf(new MethodActionComparison(left, operator, right));
            for(MethodMember m : members) {
                ret.addMember(m);
            }
            return ret;
        }
    }
}
