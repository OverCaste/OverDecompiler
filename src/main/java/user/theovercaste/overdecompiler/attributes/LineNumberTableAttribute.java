package user.theovercaste.overdecompiler.attributes;

import java.io.*;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.util.AttributeData;

public class LineNumberTableAttribute implements Attribute {
    private final LineNumberTableValue[] table;

    public LineNumberTableAttribute(LineNumberTableValue[] table) {
        this.table = table;
    }

    public LineNumberTableValue[] getTable( ) {
        return table;
    }

    public static String getName( ) {
        return "LineNumberTable";
    }

    public static Loader loader( ) {
        return new Loader();
    }

    public static class LineNumberTableValue {
        protected final int startPc;
        protected final int lineNumber;

        public LineNumberTableValue(int startPc, int lineNumber) {
            this.startPc = startPc;
            this.lineNumber = lineNumber;
        }

        public int getStartPc( ) {
            return startPc;
        }

        public int getLineNumber( ) {
            return lineNumber;
        }
    }

    public static class Loader implements AttributeLoader<LineNumberTableAttribute> {
        @Override
        public LineNumberTableAttribute load(AttributeData a, ConstantPool constantPool) throws InvalidAttributeException {
            try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.getData()))) {
                LineNumberTableValue[] table = new LineNumberTableValue[din.readUnsignedShort()];
                for (int i = 0; i < table.length; i++) {
                    table[i] = new LineNumberTableValue(din.readUnsignedShort(), din.readUnsignedShort());
                }
                return new LineNumberTableAttribute(table);
            } catch (IOException e) {
                throw new InvalidAttributeException(e);
            }
        }
    }
}
