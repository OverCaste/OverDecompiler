package user.theovercaste.overdecompiler.parseddata;

import java.util.*;

import user.theovercaste.overdecompiler.parseddata.annotation.ParsedAnnotation;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodMember;
import user.theovercaste.overdecompiler.util.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ParsedMethod implements AnnotatableElement {
    private final ClassPath returnType;
    private final String name;
    private final List<MethodMember> members = Lists.newArrayList();
    private final Set<MethodFlag> flags = Sets.newHashSet();
    private final List<ClassPath> arguments = Lists.newArrayList();
    private final List<ParsedAnnotation> annotations = Lists.newArrayList();
    private final List<ClassPath> exceptions = Lists.newArrayList();

    public ParsedMethod(ClassPath returnType, String name) {
        Preconditions.checkNotNull(name, "Name can not be null!");
        Preconditions.checkNotNull(returnType, "return type can not be null!");
        this.returnType = returnType;
        this.name = name;
    }

    public String getName( ) {
        return name;
    }

    public void addMember(MethodMember m) {
        members.add(m);
    }

    public void removeMember(MethodMember m) {
        members.remove(m);
    }

    public void addFlag(MethodFlag f) {
        flags.add(f);
    }

    public void addArgument(ClassPath argument) {
        arguments.add(argument);
    }

    @Override
    public void addAnnotation(ParsedAnnotation annotation) {
        annotations.add(annotation);
    }

    public void addException(ClassPath exception) {
        exceptions.add(exception);
    }

    public Collection<MethodMember> getMembers( ) {
        return Collections.unmodifiableCollection(members);
    }

    public Collection<MethodFlag> getFlags( ) {
        return Collections.unmodifiableCollection(flags);
    }

    public Collection<ClassPath> getArguments( ) {
        return Collections.unmodifiableCollection(arguments);
    }

    @Override
    public Collection<ParsedAnnotation> getAnnotations( ) {
        return Collections.unmodifiableCollection(annotations);
    }

    public Collection<ClassPath> getExceptions( ) {
        return Collections.unmodifiableCollection(exceptions);
    }

    public ClassPath getReturnType( ) {
        return returnType;
    }
}
