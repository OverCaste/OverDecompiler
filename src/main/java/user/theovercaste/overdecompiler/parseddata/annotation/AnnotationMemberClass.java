package user.theovercaste.overdecompiler.parseddata.annotation;

import java.util.Arrays;

import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.Preconditions;

public class AnnotationMemberClass extends AnnotationMember {
    private final String name;
    private final ClassPath classPath;

    public AnnotationMemberClass(String name, ClassPath classPath) {
        this.name = Preconditions.checkNotNull(name, "name can't be null!");
        this.classPath = Preconditions.checkNotNull(classPath, "class path can't be null!");
        Preconditions.checkArgument(!classPath.isArray(), "class path can't be an array.");
        Preconditions.checkArgument(classPath.isObject(), "class path must be an object.");
    }

    @Override
    public String getValue( ) {
        return classPath.getClassName() + ".class";
    }

    @Override
    public AnnotationMemberType getType( ) {
        return AnnotationMemberType.CLASS;
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
