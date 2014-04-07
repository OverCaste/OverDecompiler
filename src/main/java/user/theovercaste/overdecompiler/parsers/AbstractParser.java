package user.theovercaste.overdecompiler.parsers;

import java.util.Collection;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parsersteps.ParserStep;

public abstract class AbstractParser {
	public abstract ParsedClass parseClass(ClassData c) throws InvalidConstantPoolPointerException;

	public abstract Collection<ParserStep> getSteps( );
}
