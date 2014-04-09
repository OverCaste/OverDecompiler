package user.theovercaste.overdecompiler.parsers;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parsers.methodparsers.AbstractMethodParser;

public abstract class AbstractParser {
	public abstract ParsedClass parseClass(ClassData c) throws InvalidConstantPoolPointerException;

	public abstract AbstractMethodParser getMethodParser(ClassData c);
}
