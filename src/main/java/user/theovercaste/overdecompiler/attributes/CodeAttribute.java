package user.theovercaste.overdecompiler.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;

public class CodeAttribute extends ParsedAttribute {
    private final int maxStacks;
    private final int maxLocals;
    private final byte[] code;
    private final ExceptionAttribute[] exceptions;
    private final AttributeData[] attributes;

    public CodeAttribute(int maxStacks, int maxLocals, byte[] code, ExceptionAttribute[] exceptions, AttributeData[] attributes) {
        this.maxStacks = maxStacks;
        this.maxLocals = maxLocals;
        this.code = code;
        this.exceptions = exceptions;
        this.attributes = attributes;
    }

    public int getMaxStacks( ) {
        return maxStacks;
    }

    public int getMaxLocals( ) {
        return maxLocals;
    }

    public byte[] getCode( ) {
        return code;
    }

    public ExceptionAttribute[] getExceptions( ) {
        return exceptions;
    }

    public AttributeData[] getAttributes( ) {
        return attributes;
    }

    public static String getName( ) {
        return "Code";
    }

    public static Parser parser( ) {
        return new Parser();
    }

    public static class ExceptionAttribute {
        protected final int startPc;
        protected final int endPc;
        protected final int handlerPc;
        protected final int catchType;

        public ExceptionAttribute(int startPc, int endPc, int handlerPc, int catchType) {
            this.startPc = startPc;
            this.endPc = endPc;
            this.handlerPc = handlerPc;
            this.catchType = catchType;
        }
    }

    public static class Parser extends ParsedAttribute.Parser<CodeAttribute> {
        @Override
        public CodeAttribute parse(AttributeData a, ConstantPoolEntry[] constantPool) throws InvalidAttributeException {
            try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.data))) {
                int maxStack = din.readUnsignedShort();
                int maxLocals = din.readUnsignedShort();
                byte[] code = new byte[din.readInt()];
                din.readFully(code);
                ExceptionAttribute[] exceptions = new ExceptionAttribute[din.readUnsignedShort()];
                for (int i = 0; i < exceptions.length; i++) {
                    exceptions[i] = new ExceptionAttribute(din.readUnsignedShort(), din.readUnsignedShort(), din.readUnsignedShort(), din.readUnsignedShort());
                }
                AttributeData[] attributes = new AttributeData[din.readUnsignedShort()];
                for (int i = 0; i < attributes.length; i++) {
                    attributes[i] = AttributeData.loadAttribute(din);
                }
                return new CodeAttribute(maxStack, maxLocals, code, exceptions, attributes);
            } catch (IOException e) {
                throw new InvalidAttributeException(e);
            }
        }
    }
}
