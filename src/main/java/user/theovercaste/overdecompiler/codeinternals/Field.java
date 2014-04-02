package user.theovercaste.overdecompiler.codeinternals;

import user.theovercaste.overdecompiler.datahandlers.AccessFlagHandler;

public class Field {
	private final String name;
	private final ClassPath type;
	private final AccessFlagHandler flags;

	public Field(String name, ClassPath type, AccessFlagHandler flags) {
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

	public AccessFlagHandler getFlags( ) {
		return flags;
	}
}
