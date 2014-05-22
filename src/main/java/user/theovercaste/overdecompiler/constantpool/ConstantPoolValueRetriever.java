package user.theovercaste.overdecompiler.constantpool;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.WrongConstantPoolPointerTypeException;

public class ConstantPoolValueRetriever {
    public static String getNameAndTypeName(ConstantPool constantPool, int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = constantPool.get(index);
        if (entry instanceof ConstantPoolEntryNameAndType) {
            return ((ConstantPoolEntryNameAndType) entry).getName(constantPool);
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, constantPool, ConstantPoolEntryNameAndType.class);
    }

    public static String getNameAndTypeDescription(ConstantPool constantPool, int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = constantPool.get(index);
        if (entry instanceof ConstantPoolEntryNameAndType) {
            return ((ConstantPoolEntryNameAndType) entry).getDescription(constantPool);
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, constantPool, ConstantPoolEntryNameAndType.class);
    }

    public static String getString(ConstantPool constantPool, int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = constantPool.get(index);
        if (entry instanceof ConstantPoolEntryUtf8) {
            return ((ConstantPoolEntryUtf8) entry).getValue();
        }
        if (entry instanceof ConstantPoolEntryString) {
            return getString(constantPool, ((ConstantPoolEntryString) entry).getStringIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, constantPool, ConstantPoolEntryUtf8.class, ConstantPoolEntryString.class);
    }

    public static String getString(ConstantPoolEntry entry) throws InvalidConstantPoolPointerException {
        if (entry instanceof ConstantPoolEntryUtf8) {
            return ((ConstantPoolEntryUtf8) entry).getValue();
        }
        throw new WrongConstantPoolPointerTypeException("Couldn't retrieve string from non-string type: " + entry.getClass().getName());
    }

    public static String getClassName(ConstantPool constantPool, int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = constantPool.get(index);
        if (entry instanceof ConstantPoolEntryClass) {
            return ((ConstantPoolEntryClass) entry).getName(constantPool);
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, constantPool, ConstantPoolEntryClass.class);
    }
}
