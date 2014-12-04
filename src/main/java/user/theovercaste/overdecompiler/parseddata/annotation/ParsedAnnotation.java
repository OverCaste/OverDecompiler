package user.theovercaste.overdecompiler.parseddata.annotation;

import java.util.Arrays;

import user.theovercaste.overdecompiler.util.ClassPath;
import user.theovercaste.overdecompiler.util.Importable;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class ParsedAnnotation implements Importable {
    private final ClassPath type;
    private final ImmutableList<AnnotationMember> members;

    public ParsedAnnotation(ClassPath type, ImmutableList<AnnotationMember> members) {
        this.type = type;
        this.members = members;
    }

    public ClassPath getType( ) {
        return type;
    }

    public ImmutableList<AnnotationMember> getMembers( ) {
        return members;
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Iterables.concat(
                Arrays.asList(type),
                Iterables.concat(
                        Iterables.transform(members, new Function<AnnotationMember, Iterable<ClassPath>>() { // Get all of the classpaths in the member collection, concatenated with the 'type' which is also a ClassPath
                            @Override
                            public Iterable<ClassPath> apply(AnnotationMember input) {
                                return input.getImportedElements();
                            }
                        })
                        )
                );
    }
}
