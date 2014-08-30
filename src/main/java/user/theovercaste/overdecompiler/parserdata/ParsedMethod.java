package user.theovercaste.overdecompiler.parserdata;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.MethodFlag;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ParsedMethod implements AnnotatableElement {
    private final ClassPath returnType;
    private final String name;
    private final List<MethodMember> actions = Lists.newArrayList();
    private final Set<MethodFlag> flags = Sets.newHashSet();
    private final List<ClassPath> arguments = Lists.newArrayList();
    private final List<ClassPath> annotations = Lists.newArrayList();
    private final List<ClassPath> exceptions = Lists.newArrayList();

    public ParsedMethod(ClassPath returnType, String name) {
        Preconditions.checkNotNull(name, "Name can not be null!");
        this.returnType = returnType;
        this.name = name;
    }

    public Optional<ClassPath> getReturnType( ) {
        return Optional.fromNullable(returnType);
    }

    public String getName( ) {
        return name;
    }

    public void addAction(MethodMember a) {
        actions.add(a);
    }

    public void addFlag(MethodFlag f) {
        flags.add(f);
    }

    public void addArgument(ClassPath argument) {
        arguments.add(argument);
    }

    @Override
    public void addAnnotation(ClassPath annotation) {
        annotations.add(annotation);
    }
    
    public void addException(ClassPath exception) {
        exceptions.add(exception);
    }

    public Collection<MethodMember> getActions( ) {
        return Collections.unmodifiableCollection(actions);
    }

    public Collection<MethodFlag> getFlags( ) {
        return Collections.unmodifiableCollection(flags);
    }

    public Collection<ClassPath> getArguments( ) {
        return Collections.unmodifiableCollection(arguments);
    }

    @Override
    public Collection<ClassPath> getAnnotations( ) {
        return Collections.unmodifiableCollection(annotations);
    }
    
    public Collection<ClassPath> getExceptions( ) {
        return Collections.unmodifiableCollection(exceptions);
    }
}
