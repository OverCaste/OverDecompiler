package user.theovercaste.overdecompiler.parserdata;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.FieldFlag;

import com.google.common.collect.Sets;

public class ParsedField {
	private final ClassPath type;
	private final String name;
	private final Set<FieldFlag> flags = Sets.newHashSet();

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

	public Collection<FieldFlag> getFlags( ) {
		return Collections.unmodifiableCollection(flags);
	}

	public void addFlag(FieldFlag f) {
		flags.add(f);
	}
}
