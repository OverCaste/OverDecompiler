package user.theovercaste.overdecompiler.parsers.methodparsers;

import java.util.List;
import java.util.Stack;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public interface MethodParserStep {
    public abstract void parse(ClassData originClass, ParsedClass parsedClass, List<Instruction> instructions, Stack<MethodMember> value);
}