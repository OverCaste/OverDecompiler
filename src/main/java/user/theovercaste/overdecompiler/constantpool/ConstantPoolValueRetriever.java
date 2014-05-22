package user.theovercaste.overdecompiler.constantpool;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;

public class ConstantPoolValueRetriever {
    public static String getNameAndTypeName(ConstantPoolEntry[] constantPool, int index) throws InvalidConstantPoolPointerException {
        PoolPreconditions.assertPoolRange(index, constantPool.length);
        ConstantPoolEntry entry = constantPool[index];
        if (entry instanceof ConstantPoolEntryNameAndType) {
            return ((ConstantPoolEntryNameAndType) entry).getName(constantPool);
        }
        throw PoolPreconditions.getInvalidType(constantPool, index);
    }

    public static String getNameAndTypeDescription(ConstantPoolEntry[] constantPool, int index) throws InvalidConstantPoolPointerException {
        PoolPreconditions.assertPoolRange(index, constantPool.length);
        ConstantPoolEntry entry = constantPool[index];
        if (entry instanceof ConstantPoolEntryNameAndType) {
            return ((ConstantPoolEntryNameAndType) entry).getDescription(constantPool);
        }
        throw PoolPreconditions.getInvalidType(constantPool, index);
    }

    public static String getString(ConstantPoolEntry[] constantPool, int index) throws InvalidConstantPoolPointerException {
        PoolPreconditions.assertPoolRange(index, constantPool.length);
        ConstantPoolEntry entry = constantPool[index];
        if (entry instanceof ConstantPoolEntryUtf8) {
            return ((ConstantPoolEntryUtf8) entry).getValue();
        }
        if (entry instanceof ConstantPoolEntryString) {
            return getString(constantPool, ((ConstantPoolEntryString) entry).getStringIndex());
        }
        throw PoolPreconditions.getInvalidType(constantPool, index);
    }

    public static String getString(ConstantPoolEntry entry) throws InvalidConstantPoolPointerException {
        if (entry instanceof ConstantPoolEntryUtf8) {
            return ((ConstantPoolEntryUtf8) entry).getValue();
        }
        throw PoolPreconditions.getInvalidType(entry);
    }

    public static String getClassName(ConstantPoolEntry[] constantPool, int index) throws InvalidConstantPoolPointerException {
        PoolPreconditions.assertPoolRange(index, constantPool.length);
        ConstantPoolEntry entry = constantPool[index];
        if (entry instanceof ConstantPoolEntryClass) {
            return ((ConstantPoolEntryClass) entry).getName(constantPool);
        }
        throw PoolPreconditions.getInvalidType(constantPool, index);
    }
}
