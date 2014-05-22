package user.theovercaste.overdecompiler.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;

public class LineNumberTableAttribute extends ParsedAttribute {
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

    public static Parser parser( ) {
        return new Parser();
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

    public static class Parser extends ParsedAttribute.Parser<LineNumberTableAttribute> {
        @Override
        public LineNumberTableAttribute parse(AttributeData a, ConstantPoolEntry[] constantPool) throws InvalidAttributeException {
            try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.data))) {
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
