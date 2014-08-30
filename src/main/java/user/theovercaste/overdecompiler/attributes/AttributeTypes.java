package user.theovercaste.overdecompiler.attributes;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

public enum AttributeTypes {
    CODE(CodeAttribute.class, CodeAttribute.parser(), CodeAttribute.getName()),
    CONSTANT_VALUE(ConstantValueAttribute.class, ConstantValueAttribute.parser(), ConstantValueAttribute.getName()),
    LINE_NUMBER_TABLE(LineNumberTableAttribute.class, LineNumberTableAttribute.parser(), LineNumberTableAttribute.getName()),
    EXCEPTIONS(ExceptionsAttribute.class, ExceptionsAttribute.parser(), ExceptionsAttribute.getName());

    private final Class<? extends ParsedAttribute> clazz;
    private final ParsedAttribute.Parser<? extends ParsedAttribute> parser;
    private final String name;

    AttributeTypes(Class<? extends ParsedAttribute> clazz, ParsedAttribute.Parser<? extends ParsedAttribute> parser, String name) {
        this.clazz = clazz;
        this.parser = parser;
        this.name = name;
    }

    private static final ImmutableMap<Class<? extends ParsedAttribute>, String> classNameMap = createClassNameMap();
    private static final ImmutableMap<String, ParsedAttribute.Parser<?>> nameParserMap = createNameParserMap();

    private static ImmutableMap<Class<? extends ParsedAttribute>, String> createClassNameMap( ) {
        ImmutableMap.Builder<Class<? extends ParsedAttribute>, String> builder = ImmutableMap.<Class<? extends ParsedAttribute>, String> builder();
        for (AttributeTypes type : values()) {
            builder.put(type.clazz, type.name);
        }
        return builder.build();
    }

    private static ImmutableMap<String, ParsedAttribute.Parser<?>> createNameParserMap( ) {
        ImmutableMap.Builder<String, ParsedAttribute.Parser<?>> builder = ImmutableMap.<String, ParsedAttribute.Parser<?>> builder();
        for (AttributeTypes type : values()) {
            builder.put(type.name, type.parser);
        }
        return builder.build();
    }

    public static ParsedAttribute parseAttribute(AttributeData parent, ConstantPool constantPool) throws InvalidConstantPoolPointerException, InvalidAttributeException {
        String name = parent.getName(constantPool);
        ParsedAttribute.Parser<?> w = nameParserMap.get(name);
        return w.parse(parent, constantPool);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ParsedAttribute> T wrapAttribute(AttributeData a, ConstantPool constantPool, Class<T> clazz) throws InvalidConstantPoolPointerException, InvalidAttributeException {
        ParsedAttribute wrapped = parseAttribute(a, constantPool);
        if (clazz.isInstance(wrapped)) {
            return (T) wrapped;
        }
        throw new InvalidAttributeException("Attribute isn't expected type: " + wrapped.getClass().getSimpleName() + ". Expected: " + clazz.getSimpleName());
    }

    public static Optional<ParsedAttribute> getWrappedAttribute(Iterable<AttributeData> attributes, ConstantPool constantPool, String name) throws InvalidConstantPoolPointerException,
            InvalidAttributeException {
        for (AttributeData a : attributes) {
            if (a.getName(constantPool).equals(name)) {
                return Optional.of(parseAttribute(a, constantPool));
            }
        }
        return Optional.absent();
    }

    public static <T extends ParsedAttribute> Optional<T> getWrappedAttribute(Iterable<AttributeData> attributes, ConstantPool constantPool, Class<T> clazz)
            throws InvalidConstantPoolPointerException, InvalidAttributeException {
        String name = classNameMap.get(clazz);
        for (AttributeData a : attributes) {
            if (a.getName(constantPool).equals(name)) {
                return Optional.of(wrapAttribute(a, constantPool, clazz));
            }
        }
        return Optional.absent();
    }
}
