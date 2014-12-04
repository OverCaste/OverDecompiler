package user.theovercaste.overdecompiler.parseddata.annotation;

import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class AnnotationMemberArray extends AnnotationMember {
    private final String name;
    private final ImmutableList<AnnotationMember> members;

    public AnnotationMemberArray(String name, ImmutableList<AnnotationMember> members) {
        Preconditions.checkNotNull(name, "name can't be null!");
        Preconditions.checkNotNull(members, "members can't be null!");
        this.name = name;
        this.members = members;
    }

    @Override
    public String getValue( ) {
        StringBuilder b = new StringBuilder("{");
        b.append(Joiner.on(", ").join(Iterables.transform(members, new Function<AnnotationMember, String>() {
            @Override
            public String apply(AnnotationMember input) {
                return input.getValue();
            }

        })));
        b.append("}");
        return b.toString();
    }

    @Override
    public AnnotationMemberType getType( ) {
        return AnnotationMemberType.ARRAY;
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Iterables.concat(Iterables.transform(members, new Function<AnnotationMember, Iterable<ClassPath>>() {
            @Override
            public Iterable<ClassPath> apply(AnnotationMember input) {
                return input.getImportedElements();
            }
        }));
    }

    @Override
    public String getName( ) {
        return name;
    }
}
