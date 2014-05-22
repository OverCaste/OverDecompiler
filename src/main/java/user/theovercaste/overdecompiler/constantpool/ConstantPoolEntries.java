package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolEntryException;

public class ConstantPoolEntries {
    private static final int UTF8_TAG = 1;
    private static final int INTEGER_TAG = 3;
    private static final int FLOAT_TAG = 4;
    private static final int LONG_TAG = 5;
    private static final int DOUBLE_TAG = 6;
    private static final int CLASS_TAG = 7;
    private static final int STRING_TAG = 8;
    private static final int FIELD_REFERENCE_TAG = 9;
    private static final int METHOD_REFERENCE_TAG = 10;
    private static final int INTERFACE_METHOD_REFERENCE_TAG = 11;
    private static final int NAME_AND_TYPE_TAG = 12;
    private static final int METHOD_HANDLE_TAG = 15;
    private static final int METHOD_TYPE_TAG = 16;
    private static final int INVOKE_DYNAMIC_TAG = 18;

    private static final Map<Integer, ConstantPoolEntry.Factory> factories = new HashMap<>();

    static {
        factories.put(UTF8_TAG, ConstantPoolEntryUtf8.factory());
        factories.put(INTEGER_TAG, ConstantPoolEntryInteger.factory());
        factories.put(FLOAT_TAG, ConstantPoolEntryFloat.factory());
        factories.put(LONG_TAG, ConstantPoolEntryLong.factory());
        factories.put(DOUBLE_TAG, ConstantPoolEntryDouble.factory());
        factories.put(CLASS_TAG, ConstantPoolEntryClass.factory());
        factories.put(STRING_TAG, ConstantPoolEntryString.factory());
        factories.put(FIELD_REFERENCE_TAG, ConstantPoolEntryFieldReference.factory());
        factories.put(METHOD_REFERENCE_TAG, ConstantPoolEntryMethodReference.factory());
        factories.put(INTERFACE_METHOD_REFERENCE_TAG, ConstantPoolEntryInterfaceMethodReference.factory());
        factories.put(NAME_AND_TYPE_TAG, ConstantPoolEntryNameAndType.factory());
        factories.put(METHOD_HANDLE_TAG, ConstantPoolEntryMethodHandle.factory());
        factories.put(METHOD_TYPE_TAG, ConstantPoolEntryMethodType.factory());
        factories.put(INVOKE_DYNAMIC_TAG, ConstantPoolEntryInvokeDynamic.factory());
    }

    public static ConstantPoolEntry readEntry(DataInputStream din) throws InvalidConstantPoolEntryException, IOException {
        try {
            int tag = din.readUnsignedByte();
            if (!factories.containsKey(tag)) {
                throw new InvalidConstantPoolEntryException("Invalid tag: " + tag);
            }
            ConstantPoolEntry.Factory b = factories.get(tag);
            b.read(tag, din);
            return b.build();
        } catch (IOException e) {
            throw e;
        }
    }
}
