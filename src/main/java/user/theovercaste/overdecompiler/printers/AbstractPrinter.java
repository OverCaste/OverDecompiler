package user.theovercaste.overdecompiler.printers;

import java.io.IOException;
import java.io.OutputStream;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.ClassType;
import user.theovercaste.overdecompiler.parsers.ParsedClass;
import user.theovercaste.overdecompiler.parsers.ParsedMethod;

public abstract class AbstractPrinter {
	public abstract void print(ParsedClass c, OutputStream out) throws IOException;

	public boolean isEnum(ParsedClass c) {
		if (c.getType().equals(ClassType.ENUM)) {
			return true;
		}
		if (c.getParent().equals(ClassPath.OBJECT_ENUM)) {
			return true;
		}
		return false;
	}

	protected String getArgumentName(ParsedClass clazz, ParsedMethod m, ClassPath arg) {
		return "derp";
	}
}
