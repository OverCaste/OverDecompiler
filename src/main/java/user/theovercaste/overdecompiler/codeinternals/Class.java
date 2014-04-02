package user.theovercaste.overdecompiler.codeinternals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import user.theovercaste.overdecompiler.datahandlers.ImportList;

public class Class {
	private final String name;
	private final ClassType type;
	private final ClassPath parent;
	private final ArrayList<ClassFlag> flags;
	private final ArrayList<Field> fields;
	private final ArrayList<Method> methods;
	private final ArrayList<Class> nestedClasses;

	public Class(String name, ClassType type, ClassPath parent) {
		this.name = name;
		this.type = type;
		this.parent = parent;
		flags = new ArrayList<ClassFlag>();
		fields = new ArrayList<Field>();
		methods = new ArrayList<Method>();
		nestedClasses = new ArrayList<Class>();
	}

	public String getName( ) {
		return name;
	}

	public ClassType getType( ) {
		return type;
	}

	public ClassPath getParent( ) {
		return parent;
	}

	public Iterable<ClassFlag> getFlags( ) {
		return Collections.unmodifiableList(flags);
	}

	public Iterable<Field> getFields( ) {
		return Collections.unmodifiableList(fields);
	}

	public Iterable<Method> getMethods( ) {
		return Collections.unmodifiableList(methods);
	}

	public Iterable<Class> getNestedClasses( ) {
		return Collections.unmodifiableList(nestedClasses);
	}

	public void addFlag(ClassFlag f) {
		flags.add(f);
	}

	public void addField(Field f) {
		fields.add(f);
	}

	public void addMethod(Method m) {
		methods.add(m);
	}

	public void addNestedClass(Class c) {
		nestedClasses.add(c);
	}

	public void addFlags(Collection<? extends ClassFlag> flags) {
		this.flags.addAll(flags);
	}

	public void addFields(Collection<? extends Field> fields) {
		this.fields.addAll(fields);
	}

	public void addMethods(Collection<? extends Method> methods) {
		this.methods.addAll(methods);
	}

	public void addNestedClasses(Collection<? extends Class> nestedClasses) {
		this.nestedClasses.addAll(nestedClasses);
	}

	/**
	 * Creates a class header string based on it's defined attributes.
	 * 
	 * @param flags
	 * @return (public) (final) [(abstract )class/enum/interface/@interface] [name] {
	 */
	public String getClassDefinitionString(ImportList imports) {
		StringBuilder sb = new StringBuilder();

		if (flags.contains(ClassFlag.PUBLIC)) {
			sb.append("public ");
		}
		if (flags.contains(ClassFlag.FINAL)) {
			sb.append("final ");
		}
		switch (type) {
			case ENUM:
				sb.append("enum");
				break;
			case INTERFACE:
				sb.append("interface");
				break;
			case ANNOTATION:
				sb.append("@interface");
				break;
			case ABSTRACT_CLASS:
				sb.append("abstract "); // Fall through to create 'abstract class'
			case CLASS:
			default:
				sb.append("class");
				break;
		}
		sb.append(" ").append(name).append(" ");
		String superPath = parent.getSimplePath();
		if ((parent != null) && !"java.lang.Object".equals(superPath)) {
			imports.addQualifiedPath(superPath);
			sb.append("extends ").append(imports.getQualifiedName(superPath)).append(" ");
		}
		sb.append("{");
		return sb.toString();
	}
}
