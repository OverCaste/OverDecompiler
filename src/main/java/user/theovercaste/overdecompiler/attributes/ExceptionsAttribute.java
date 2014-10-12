package user.theovercaste.overdecompiler.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;

public class ExceptionsAttribute extends ParsedAttribute {
    private final int[] exceptions;

    public ExceptionsAttribute(int[] exceptions) {
        this.exceptions = exceptions;
    }

    public int[] getExceptions( ) {
        return exceptions;
    }

    public static String getName( ) {
        return "Exceptions";
    }

    public static Parser parser( ) {
        return new Parser();
    }

    public static class Parser extends ParsedAttribute.Parser<ExceptionsAttribute> {
        @Override
        public ExceptionsAttribute parse(AttributeData a, ConstantPool constantPool) throws InvalidAttributeException {
            try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.data))) {
                int exceptionsCount = din.readUnsignedShort();
                int[] exceptions = new int[exceptionsCount];
                for(int i = 0; i < exceptionsCount; i++) {
                    exceptions[i] = din.readUnsignedShort();
                }
                return new ExceptionsAttribute(exceptions);
            } catch (IOException e) {
                throw new InvalidAttributeException(e);
            }
        }
    }
}
