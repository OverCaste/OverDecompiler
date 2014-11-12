package user.theovercaste.overdecompiler.parsers.javaparser.methodparsers;

import java.util.*;

import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodBlock;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodMember;

/**
 *
 */
public abstract class MethodBlockContainer {
    private final List<Member> members;

    public MethodBlockContainer(List<Member> members) {
        this.members = new ArrayList<>(members);
    }

    /**
     * @return A mutable list of this container's members.
     */
    public List<Member> getMembers( ) {
        return members;
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public abstract MethodBlock toMethodBlock(List<MethodMember> members, Stack<MethodActionPointer> pointerStack);

    public static class Member {
        private final Instruction instruction;
        private final MethodBlockContainer container;

        public Member(Instruction instruction, MethodBlockContainer container) {
            this.instruction = instruction;
            this.container = container;
        }

        public boolean isInstruction( ) {
            return instruction != null;
        }

        public boolean isContainer( ) {
            return container != null;
        }

        public Instruction getInstruction( ) {
            return instruction;
        }

        public MethodBlockContainer getContainer( ) {
            return container;
        }
    }
}
