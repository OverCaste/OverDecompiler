package user.theovercaste.overdecompiler.parsers.methodparsers;

import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.attributes.CodeAttribute;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public abstract class AbstractMethodParser {
	protected CodeAttribute getCodeAttribute(ClassData fromClass, AttributeData[] attributes) throws InvalidConstantPoolPointerException {
		for (AttributeData d : attributes) {
			if (d.getName(fromClass.getConstantPool()).equals("Code")) {
				return CodeAttribute.wrapper().wrap(d, fromClass.getConstantPool());
			}
		}
		return null;
	}

	public abstract void parseMethodActions(ClassData fromClass, ParsedClass toClass, MethodData origin, ParsedMethod value) throws InvalidConstantPoolPointerException;
}
