package user.theovercaste.overdecompiler.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;

public class ConstantValueAttribute extends ParsedAttribute {
    private final int attributeIndex;

    public ConstantValueAttribute(int attributeIndex) {
        this.attributeIndex = attributeIndex;
    }

    public int getAttributeIndex( ) {
        return attributeIndex;
    }

    public static String getName( ) {
        return "ConstantValue";
    }

    public static Parser parser( ) {
        return new Parser();
    }

    public static class Parser extends ParsedAttribute.Parser<ConstantValueAttribute> {
        @Override
        public ConstantValueAttribute parse(AttributeData a, ConstantPool constantPool) throws InvalidAttributeException {
            try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.data))) {
                return new ConstantValueAttribute(din.readUnsignedShort());
            } catch (IOException e) {
                throw new InvalidAttributeException(e);
            }
        }
    }
}
