package user.theovercaste.overdecompiler.parserdata;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.MethodFlag;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ParsedMethod {
	private final ClassPath returnType;
	private final String name;
	private final List<MethodAction> actions = Lists.newArrayList();
	private final Set<MethodFlag> flags = Sets.newHashSet();
	private final List<ClassPath> arguments = Lists.newArrayList();

	public ParsedMethod(ClassPath returnType, String name) {
		this.returnType = returnType;
		this.name = name;
	}

	public Optional<ClassPath> getReturnType( ) {
		return Optional.fromNullable(returnType);
	}

	public String getName( ) {
		return name;
	}

	public void addAction(MethodAction a) {
		actions.add(a);
	}

	public void addFlag(MethodFlag f) {
		flags.add(f);
	}

	public void addArgument(ClassPath argument) {
		arguments.add(argument);
	}

	public Collection<MethodAction> getActions( ) {
		return Collections.unmodifiableCollection(actions);
	}

	public Collection<MethodFlag> getFlags( ) {
		return Collections.unmodifiableCollection(flags);
	}

	public Collection<ClassPath> getArguments( ) {
		return Collections.unmodifiableCollection(arguments);
	}
}
