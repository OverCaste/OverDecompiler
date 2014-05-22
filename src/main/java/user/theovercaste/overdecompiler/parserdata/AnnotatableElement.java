package user.theovercaste.overdecompiler.parserdata;

import java.util.Collection;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;

public interface AnnotatableElement {
    public void addAnnotation(ClassPath annotation);

    public Collection<ClassPath> getAnnotations( );
}
