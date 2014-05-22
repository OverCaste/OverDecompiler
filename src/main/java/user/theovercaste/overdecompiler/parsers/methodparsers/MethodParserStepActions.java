package user.theovercaste.overdecompiler.parsers.methodparsers;

import java.util.List;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.method.MethodBlock;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public class MethodParserStepActions implements MethodParserStep {
    @Override
    public void parse(ClassData originClass, ParsedClass parsedClass, List<Instruction> instructions, Stack<MethodMember> value) {
        for (Instruction i : instructions) {
            if (i.isAction()) {
                try {
                    value.add(i.getAction(originClass, value));
                } catch (InstructionParsingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseBlock(ClassData originClass, ParsedClass parsedClass, MethodBlock b) {
        Stack<MethodMember> stack = new Stack<MethodMember>();
        stack.addAll(b.getMembers());
        for (Instruction i : b.getInstructions()) {
            if (i.isAction()) {
                try {
                    MethodMember m = i.getAction(originClass, stack);
                    stack.push(m);
                } catch (InstructionParsingException e) {
                    e.printStackTrace();
                }
            }
        }
        for (MethodMember m : stack) {
            b.addMember(m);
        }
        for (MethodMember m : b.getMembers()) {
            if (m.getType() == MethodMember.Type.BLOCK) {
                parseBlock(originClass, parsedClass, (MethodBlock) m);
            }
        }
    }
}
