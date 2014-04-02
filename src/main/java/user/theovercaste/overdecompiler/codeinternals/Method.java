package user.theovercaste.overdecompiler.codeinternals;

import java.util.Optional;

import user.theovercaste.overdecompiler.datahandlers.MethodFlagHandler;

public class Method {
	private final ClassPath returnType;
	private final String name;
	private final MethodFlagHandler flags;

	public Method(ClassPath returnType, String name, MethodFlagHandler flags) {
		this.returnType = returnType;
		this.name = name;
		this.flags = flags;
	}

	public Optional<ClassPath> getReturnType( ) {
		return Optional.ofNullable(returnType);
	}

	public String getName( ) {
		return name;
	}

	public MethodFlagHandler getFlags( ) {
		return flags;
	}
}
