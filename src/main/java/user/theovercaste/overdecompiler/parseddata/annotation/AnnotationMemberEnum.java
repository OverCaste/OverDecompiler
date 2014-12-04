package user.theovercaste.overdecompiler.parseddata.annotation;

import java.util.Arrays;

import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.Preconditions;

public class AnnotationMemberEnum extends AnnotationMember {
    private final String name;
    private final ClassPath classPath;
    private final String field;

    public AnnotationMemberEnum(String name, ClassPath classPath, String field) {
        this.name = Preconditions.checkNotNull(name, "name can't be null!");
        this.classPath = Preconditions.checkNotNull(classPath, "class path can't be null!");
        this.field = Preconditions.checkNotNull(field, "field can't be null!");
        Preconditions.checkArgument(!classPath.isArray(), "class path can't be an array.");
        Preconditions.checkArgument(classPath.isObject(), "class path must be an object.");
    }

    @Override
    public String getValue( ) {
        return classPath.getClassName() + "." + field;
    }

    @Override
    public AnnotationMemberType getType( ) {
        return AnnotationMemberType.ENUM;
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Arrays.asList(classPath);
    }

    @Override
    public String getName( ) {
        return name;
    }
}
