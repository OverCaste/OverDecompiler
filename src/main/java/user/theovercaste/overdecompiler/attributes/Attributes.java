package user.theovercaste.overdecompiler.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class Attributes {
	private static final HashMap<String, Attribute.Wrapper<?>> nameMap = new HashMap<>();

	static {
		nameMap.put("Code", CodeAttribute.wrapper());
		nameMap.put("ConstantValue", ConstantValueAttribute.wrapper());
	}

	public static Attribute loadAttribute(DataInputStream din) throws IOException {
		return Attribute.readAttribute(din);
	}

	public static Attribute wrapAttribute(Attribute parent, ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		String name = parent.getName(constantPool);
		Attribute.Wrapper<?> w = nameMap.get(name);
		return w.wrap(parent, constantPool);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Attribute> T wrapAttribute(Attribute a, ConstantPoolEntry[] constantPool, Class<T> clazz) throws InvalidConstantPoolPointerException {
		Attribute wrapped = wrapAttribute(a, constantPool);
		if (clazz.isInstance(wrapped)) {
			return (T) wrapped;
		}
		throw new InvalidAttributeException("Attribute isn't expected type: " + wrapped.getClass().getSimpleName() + ". Expected: " + clazz.getSimpleName());
	}
}
