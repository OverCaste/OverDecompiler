package user.theovercaste.overdecompiler.attributes;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.util.AttributeData;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

public class Attributes {
    private static final Logger logger = LoggerFactory.getLogger(Attributes.class);

    private static enum AttributeType {
        CODE(CodeAttribute.class, CodeAttribute.getName(), CodeAttribute.loader()),
        LINE_NUMBER_TABLE(LineNumberTableAttribute.class, LineNumberTableAttribute.getName(), LineNumberTableAttribute.loader()),
        RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE(RuntimeVisibleAnnotationsAttribute.class, RuntimeVisibleAnnotationsAttribute.getName(), RuntimeVisibleAnnotationsAttribute.loader()),
        EXCEPTIONS(ExceptionsAttribute.class, ExceptionsAttribute.getName(), ExceptionsAttribute.loader());

        public final Class<? extends Attribute> clazz;
        public final String name;
        public final AttributeLoader<? extends Attribute> loader;

        AttributeType(Class<? extends Attribute> clazz, String name, AttributeLoader<? extends Attribute> loader) {
            this.clazz = clazz;
            this.name = name;
            this.loader = loader;
        }
    }

    private static final ImmutableMap<Class<? extends Attribute>, AttributeType> attributeClassMap = createAttributeClassMap();

    private static ImmutableMap<Class<? extends Attribute>, AttributeType> createAttributeClassMap( ) {
        ImmutableMap.Builder<Class<? extends Attribute>, AttributeType> builder = ImmutableMap.builder();
        for (AttributeType type : AttributeType.values()) {
            builder.put(type.clazz, type);
        }
        return builder.build();
    }

    public static <T extends Attribute> Optional<T> loadAttribute(Collection<? extends AttributeData> rawAttributeData, ConstantPool constantPool, Class<T> token) throws InvalidAttributeException {
        AttributeType attributeType = attributeClassMap.get(token);
        if (attributeType == null) {
            throw new InvalidAttributeException("Attempted to get an attribute which had an invalid type! (" + token.getName() + ")");
        }
        for (AttributeData d : rawAttributeData) {
            try {
                if (attributeType.name.equals(d.getName(constantPool))) {
                    @SuppressWarnings("unchecked")
                    // The loader contract prohibits this from being a problem.
                    Optional<T> ret = (Optional<T>) Optional.of(attributeType.loader.load(d, constantPool));
                    return ret;
                }
            } catch (InvalidConstantPoolPointerException e) {
                logger.warn("Attempted to load an invalid attribute: {}", token.getClass().getName(), e);
            }
        }
        return Optional.absent();
    }
}
