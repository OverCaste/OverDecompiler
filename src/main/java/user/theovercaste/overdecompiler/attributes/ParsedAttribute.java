package user.theovercaste.overdecompiler.attributes;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;

public abstract class ParsedAttribute {
    public static abstract class Parser<T extends ParsedAttribute> {
        public abstract T parse(AttributeData a, ConstantPoolEntry[] constantPool) throws InvalidAttributeException;
    }
}
