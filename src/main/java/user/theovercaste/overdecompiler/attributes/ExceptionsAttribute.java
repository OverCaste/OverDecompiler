package user.theovercaste.overdecompiler.attributes;

import java.io.*;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.util.AttributeData;

public class ExceptionsAttribute implements Attribute {
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

    public static Loader loader( ) {
        return new Loader();
    }

    public static class Loader implements AttributeLoader<ExceptionsAttribute> {
        @Override
        public ExceptionsAttribute load(AttributeData a, ConstantPool constantPool) throws InvalidAttributeException {
            try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.getData()))) {
                int exceptionsCount = din.readUnsignedShort();
                int[] exceptions = new int[exceptionsCount];
                for (int i = 0; i < exceptionsCount; i++) {
                    exceptions[i] = din.readUnsignedShort();
                }
                return new ExceptionsAttribute(exceptions);
            } catch (IOException e) {
                throw new InvalidAttributeException(e);
            }
        }
    }
}
