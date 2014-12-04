package user.theovercaste.overdecompiler.parseddata;

import java.util.*;

import user.theovercaste.overdecompiler.parseddata.annotation.ParsedAnnotation;
import user.theovercaste.overdecompiler.util.*;

import com.google.common.base.Function;
import com.google.common.collect.*;

public class ParsedField implements AnnotatableElement, Importable {
    private final ClassPath type;
    private final String name;
    private final Set<FieldFlag> flags = Sets.newHashSet();
    private final List<ParsedAnnotation> annotations = Lists.newArrayList();

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
    public void addAnnotation(ParsedAnnotation annotation) {
        annotations.add(annotation);
    }

    public Collection<FieldFlag> getFlags( ) {
        return Collections.unmodifiableCollection(flags);
    }

    @Override
    public Collection<ParsedAnnotation> getAnnotations( ) {
        return Collections.unmodifiableCollection(annotations);
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Iterables.concat(
                Arrays.asList(type),
                Iterables.concat(Iterables.transform(annotations, new Function<ParsedAnnotation, Iterable<ClassPath>>() {
                    @Override
                    public Iterable<ClassPath> apply(ParsedAnnotation input) {
                        return input.getImportedElements();
                    }
                }))
                );
    }
}
