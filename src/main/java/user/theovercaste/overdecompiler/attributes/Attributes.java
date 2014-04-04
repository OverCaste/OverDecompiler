package user.theovercaste.overdecompiler.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class Attributes {
	private static final HashMap<String, AttributeData.Wrapper<?>> nameMap = new HashMap<>();

	static {
		nameMap.put("Code", CodeAttribute.wrapper());
		nameMap.put("ConstantValue", ConstantValueAttribute.wrapper());
	}

	public static AttributeData loadAttribute(DataInputStream din) throws IOException {
		return AttributeData.readAttribute(din);
	}

	public static AttributeData wrapAttribute(AttributeData parent, ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		String name = parent.getName(constantPool);
		AttributeData.Wrapper<?> w = nameMap.get(name);
		return w.wrap(parent, constantPool);
	}

	@SuppressWarnings("unchecked")
	public static <T extends AttributeData> T wrapAttribute(AttributeData a, ConstantPoolEntry[] constantPool, Class<T> clazz) throws InvalidConstantPoolPointerException {
		AttributeData wrapped = wrapAttribute(a, constantPool);
		if (clazz.isInstance(wrapped)) {
			return (T) wrapped;
		}
		throw new InvalidAttributeException("Attribute isn't expected type: " + wrapped.getClass().getSimpleName() + ". Expected: " + clazz.getSimpleName());
	}
}
