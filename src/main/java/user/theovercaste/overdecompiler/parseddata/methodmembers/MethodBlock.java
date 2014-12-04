package user.theovercaste.overdecompiler.parseddata.methodmembers;

import java.util.ArrayList;
import java.util.List;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class MethodBlock extends MethodMember {
    protected final List<MethodMember> members = new ArrayList<>();

    public MethodBlock( ) {
        super(MethodMember.Type.BLOCK);
    }

    public abstract String getBlockHeader(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx);

    public ImmutableList<MethodMember> getMembers( ) {
        return ImmutableList.copyOf(members); // defensive copy
    }

    public void addMember(MethodMember m) {
        members.add(m);
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Iterables.concat(Iterables.transform(members, new Function<MethodMember, Iterable<ClassPath>>() {
            @Override
            public Iterable<ClassPath> apply(MethodMember input) {
                return input.getImportedElements();
            }
        }));
    }

    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        for (MethodMember m : members) {
            m.countReferences(printingContext);
        }
    }
}
