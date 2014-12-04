package user.theovercaste.overdecompiler.parseddata.annotation;

import java.util.Collections;

import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.Preconditions;

public class AnnotationMemberConstant extends AnnotationMember {
    private final String name;
    private final String value;
    private final AnnotationMemberType type;

    public AnnotationMemberConstant(String name, String value, AnnotationMemberType type) {
        Preconditions.checkNotNull(value, "value can't be null!");
        Preconditions.checkNotNull(type, "type can't be null!");
        this.name = name;
        this.value = value;
        this.type = type;
    }

    @Override
    public String getName( ) {
        return name;
    }

    @Override
    public String getValue( ) {
        return value;
    }

    @Override
    public AnnotationMemberType getType( ) {
        return type;
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Collections.emptyList();
    }
}
