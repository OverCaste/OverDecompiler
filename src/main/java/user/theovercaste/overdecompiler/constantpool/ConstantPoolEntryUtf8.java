package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.base.Charsets;

public class ConstantPoolEntryUtf8 extends ConstantPoolEntry {
    protected final byte[] data;

    public ConstantPoolEntryUtf8(byte[] data) {
        super(ConstantPoolEntries.UTF8_TAG);
        this.data = data;
    }

    public ConstantPoolEntryUtf8(String string) {
        this(string.getBytes(Charsets.UTF_8));
    }

    public byte[] getData( ) {
        return data;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public String getValue( ) {
        return new String(data, Charsets.UTF_8);
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected byte[] data;
        protected int length;

        @Override
        public void read(DataInputStream din) throws IOException {
            length = din.readUnsignedShort();
            data = new byte[length];
            din.readFully(data);
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryUtf8(data);
        }
    }
}
