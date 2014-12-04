package user.theovercaste.overdecompiler.parseddata.annotation;

import java.util.Arrays;

import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class AnnotationMemberAnnotation extends AnnotationMember {
    private final String name;
    private final ClassPath type;
    private final ImmutableList<AnnotationMember> members;

    public AnnotationMemberAnnotation(String name, ClassPath type, ImmutableList<AnnotationMember> members) {
        Preconditions.checkNotNull(name, "name can't be null!");
        Preconditions.checkNotNull(type, "type can't be null!");
        Preconditions.checkNotNull(members, "members can't be null!");
        Preconditions.checkArgument(!type.isArray(), "type can't be an array type!");
        Preconditions.checkArgument(type.isObject(), "type must be an object.");
        this.name = name;
        this.type = type;
        this.members = members;
    }

    public AnnotationMemberAnnotation(String name, ParsedAnnotation parsedAnnotation) {
        this(name,
                Preconditions.checkNotNull(parsedAnnotation, "parsed annotation can't be null!").getType(),
                parsedAnnotation.getMembers());
    }

    @Override
    public String getValue( ) {
        StringBuilder b = new StringBuilder("@");
        b.append(type.getClassName());
        if (members.size() != 0) {
            b.append("(");
            b.append(Joiner.on(", ").join(Iterables.transform(members, new Function<AnnotationMember, String>() {
                @Override
                public String apply(AnnotationMember input) {
                    return input.getValue();
                }

            })));
            b.append(")");
        }
        return b.toString();
    }

    @Override
    public AnnotationMemberType getType( ) {
        return AnnotationMemberType.ANNOTATION;
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Iterables.concat(Arrays.asList(type), Iterables.concat(Iterables.transform(members, new Function<AnnotationMember, Iterable<ClassPath>>() {
            @Override
            public Iterable<ClassPath> apply(AnnotationMember input) {
                return input.getImportedElements();
            }
        })));
    }

    @Override
    public String getName( ) {
        return name;
    }
}
