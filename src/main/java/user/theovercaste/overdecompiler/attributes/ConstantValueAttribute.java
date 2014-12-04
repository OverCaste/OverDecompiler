package user.theovercaste.overdecompiler.attributes;

import java.io.*;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.util.AttributeData;

public class ConstantValueAttribute implements Attribute {
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

    public static class Parser implements AttributeLoader<ConstantValueAttribute> {
        @Override
        public ConstantValueAttribute load(AttributeData a, ConstantPool constantPool) throws InvalidAttributeException {
            try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.getData()))) {
                return new ConstantValueAttribute(din.readUnsignedShort());
            } catch (IOException e) {
                throw new InvalidAttributeException(e);
            }
        }
    }
}
