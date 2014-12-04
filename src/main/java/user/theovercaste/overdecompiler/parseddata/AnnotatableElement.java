package user.theovercaste.overdecompiler.parseddata;

import java.util.Collection;

import user.theovercaste.overdecompiler.parseddata.annotation.ParsedAnnotation;

public interface AnnotatableElement {
    public void addAnnotation(ParsedAnnotation annotation);

    public Collection<ParsedAnnotation> getAnnotations( );
}
