package user.theovercaste.overdecompiler.parserdata;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.FieldFlag;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ParsedField implements AnnotatableElement {
    private final ClassPath type;
    private final String name;
    private final Set<FieldFlag> flags = Sets.newHashSet();
    private final List<ClassPath> annotations = Lists.newArrayList();

    public ParsedField(ClassPath type, String name) {
        this.type = type;
        this.name = name;
    }

    public ClassPath getType( ) {
        return type;
    }

    public String getName( ) {
        return name;
    }

    public void addFlag(FieldFlag f) {
        flags.add(f);
    }

    @Override
    public void addAnnotation(ClassPath annotation) {
        annotations.add(annotation);
    }

    public Collection<FieldFlag> getFlags( ) {
        return Collections.unmodifiableCollection(flags);
    }

    @Override
    public Collection<ClassPath> getAnnotations( ) {
        return Collections.unmodifiableCollection(annotations);
    }
}
