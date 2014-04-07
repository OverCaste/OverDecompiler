package user.theovercaste.overdecompiler.codeinternals;

import user.theovercaste.overdecompiler.datahandlers.ClassFlagHandler;

public class Field {
	private final String name;
	private final ClassPath type;
	private final ClassFlagHandler flags;

	public Field(String name, ClassPath type, ClassFlagHandler flags) {
		this.name = name;
		this.type = type;
		this.flags = flags;
	}

	public String getName( ) {
		return name;
	}

	public ClassPath getType( ) {
		return type;
	}

	public ClassFlagHandler getFlags( ) {
		return flags;
	}
}
