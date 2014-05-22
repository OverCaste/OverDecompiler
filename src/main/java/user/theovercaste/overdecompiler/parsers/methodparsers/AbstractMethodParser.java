package user.theovercaste.overdecompiler.parsers.methodparsers;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public abstract class AbstractMethodParser {
    public abstract void parseMethodActions(ClassData fromClass, ParsedClass toClass, MethodData origin, ParsedMethod value) throws InvalidConstantPoolPointerException, InvalidAttributeException;
}
