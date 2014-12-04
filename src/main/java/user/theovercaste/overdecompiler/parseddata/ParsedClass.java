package user.theovercaste.overdecompiler.parseddata;

import java.util.*;

import user.theovercaste.overdecompiler.parseddata.annotation.ParsedAnnotation;
import user.theovercaste.overdecompiler.util.*;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ParsedClass implements AnnotatableElement {
    private final String name;
    private final String packageValue; // Keyword again
    private final ClassType type;
    private final JavaVersion javaVersion;
    private ClassPath parent;
    private final Set<ClassFlag> flags = Sets.newHashSet();
    private final ImportList imports = new ImportList();
    private final Set<ClassPath> interfaces = Sets.newHashSet();
    private final Set<ParsedMethod> methods = Sets.newHashSet();
    private final Set<ParsedMethod> constructors = Sets.newHashSet();
    private final Set<ParsedField> fields = Sets.newHashSet();
    private final Set<ParsedClass> nestedClasses = Sets.newHashSet();
    private final List<ParsedAnnotation> annotations = Lists.newArrayList();

    public ParsedClass(JavaVersion javaVersion, String name, String packageValue, ClassType type) {
        Preconditions.checkNotNull(javaVersion);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(packageValue);
        Preconditions.checkNotNull(type);
        this.javaVersion = javaVersion;
        this.name = name;
        this.packageValue = packageValue;
        this.type = type;
    }

    public String getName( ) {
        return name;
    }

    public String getPackage( ) {
        return packageValue;
    }

    public JavaVersion getJavaVersion( ) {
        return javaVersion;
    }

    public ClassType getType( ) {
        return type;
    }

    public Optional<ClassPath> getParent( ) {
        return Optional.fromNullable(parent);
    }

    public void setParent(ClassPath parent) {
        this.parent = parent;
    }

    public void addImport(String i) {
        imports.add(i);
    }

    public boolean removeImport(String i) {
        return imports.remove(i);
    }

    public void addInterface(ClassPath i) {
        interfaces.add(i);
    }

    public void addMethod(ParsedMethod m) {
        methods.add(m);
    }

    public void addConstructor(ParsedMethod c) {
        constructors.add(c);
    }

    public void removeConstructor(ParsedMethod c) {
        constructors.remove(c);
    }

    public void addField(ParsedField f) {
        fields.add(f);
    }

    public void addNestedClass(ParsedClass c) {
        nestedClasses.add(c);
    }

    public void addFlag(ClassFlag f) {
        flags.add(f);
    }

    @Override
    public void addAnnotation(ParsedAnnotation annotation) { // TODO actual annotation class, annotation params
        annotations.add(annotation);
    }

    public Collection<String> getImports( ) {
        return Collections.unmodifiableCollection(imports);
    }

    public Collection<ClassPath> getInterfaces( ) {
        return Collections.unmodifiableCollection(interfaces);
    }

    public Collection<ParsedMethod> getMethods( ) {
        return Collections.unmodifiableCollection(methods);
    }

    public Collection<ParsedMethod> getConstructors( ) {
        return Collections.unmodifiableCollection(constructors);
    }

    public Collection<ParsedField> getFields( ) {
        return Collections.unmodifiableCollection(fields);
    }

    public Collection<ParsedClass> getNestedClasses( ) {
        return Collections.unmodifiableCollection(nestedClasses);
    }

    public Collection<ClassFlag> getFlags( ) {
        return Collections.unmodifiableCollection(flags);
    }

    @Override
    public Collection<ParsedAnnotation> getAnnotations( ) {
        return Collections.unmodifiableCollection(annotations);
    }

    public ClassPath getClassPath( ) {
        return ClassPath.valueOf(getPackage(), getName());
    }
}
