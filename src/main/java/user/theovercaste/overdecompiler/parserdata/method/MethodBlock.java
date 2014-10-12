package user.theovercaste.overdecompiler.parserdata.method;

import java.util.ArrayList;
import java.util.List;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

import com.google.common.collect.ImmutableList;

public abstract class MethodBlock extends MethodMember {
    protected final List<MethodMember> members = new ArrayList<>();

    public MethodBlock( ) {
        super(MethodMember.Type.BLOCK);
    }

    public abstract String getBlockHeader(ParsedClass c, ParsedMethod parent);

    public ImmutableList<MethodMember> getMembers( ) {
        return ImmutableList.copyOf(members); // defensive copy
    }

    public void addMember(MethodMember m) {
        members.add(m);
    }
}
