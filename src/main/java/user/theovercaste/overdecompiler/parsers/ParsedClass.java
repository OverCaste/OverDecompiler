package user.theovercaste.overdecompiler.parsers;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import user.theovercaste.overdecompiler.codeinternals.ClassFlag;
import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.ClassType;

import com.google.common.collect.Sets;

public class ParsedClass {
	private final String name;
	private final String packageValue; // Keyword again
	private final ClassType type;
	private final ClassPath parent;
	private final Set<ClassFlag> flags = Sets.newHashSet();
	private final Set<ClassPath> imports = Sets.newHashSet();
	private final Set<ClassPath> interfaces = Sets.newHashSet();
	private final Set<ParsedMethod> methods = Sets.newHashSet();
	private final Set<ParsedField> fields = Sets.newHashSet();
	private final Set<ParsedClass> nestedClasses = Sets.newHashSet();

	public ParsedClass(String name, String packageValue, ClassType type, ClassPath parent) {
		this.name = name;
		this.packageValue = packageValue;
		this.type = type;
		this.parent = parent;
	}

	public String getName( ) {
		return name;
	}

	public String getPackage( ) {
		return packageValue;
	}

	public ClassType getType( ) {
		return type;
	}

	public ClassPath getParent( ) {
		return parent;
	}

	public Collection<ClassPath> getImports( ) {
		return Collections.unmodifiableCollection(imports);
	}

	public Collection<ClassPath> getInterfaces( ) {
		return Collections.unmodifiableCollection(interfaces);
	}

	public Collection<ParsedMethod> getMethods( ) {
		return Collections.unmodifiableCollection(methods);
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

	public void addImport(ClassPath i) {
		imports.add(i);
	}

	public void addInterface(ClassPath i) {
		interfaces.add(i);
	}

	public void addMethod(ParsedMethod m) {
		methods.add(m);
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

	public ClassPath getClassPath( ) {
		StringBuilder currentObject = new StringBuilder(getPackage());
		if (currentObject.length() > 0) {
			currentObject.append(".");
		}
		currentObject.append(getName());
		return new ClassPath(currentObject.toString());
	}
}
