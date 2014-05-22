package user.theovercaste.overdecompiler.parserdata.method;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

import com.google.common.collect.ImmutableList;

public abstract class MethodBlock extends MethodMember {
    protected final List<MethodMember> members = new ArrayList<>();
    protected final List<Instruction> instructions = new ArrayList<>();

    public MethodBlock( ) {
        super(Type.BLOCK);
    }

    public abstract String getBlockHeader(ParsedClass c, ParsedMethod parent);

    public ImmutableList<MethodMember> getMembers( ) {
        return ImmutableList.copyOf(members); // defensive copy
    }

    public void addMember(MethodMember m) {
        members.add(m);
    }

    public ImmutableList<Instruction> getInstructions( ) {
        return ImmutableList.copyOf(instructions);
    }

    @Override
    public void print(ParsedClass c, ParsedMethod parent, PrintStream out) {
        // TODO
    }
}
