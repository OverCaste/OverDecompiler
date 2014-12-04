package user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers;

import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;
import user.theovercaste.overdecompiler.rawclassdata.MethodData;

public abstract class AbstractMethodParser {
    public abstract void parseMethodActions(ClassData fromClass, ParsedClass toClass, MethodData origin, ParsedMethod value) throws InvalidConstantPoolPointerException, InvalidAttributeException;
}
