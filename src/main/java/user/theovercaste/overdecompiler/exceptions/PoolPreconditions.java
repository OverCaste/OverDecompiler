package user.theovercaste.overdecompiler.exceptions;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;

public class PoolPreconditions {
    public static void assertPoolRange(int index, int size) throws InvalidConstantPoolPointerIndexException {
        if (index < 0) {
            throw new InvalidConstantPoolPointerIndexException("Constant pool index is less than zero: " + index + ".");
        }
        if (index >= size) {
            throw new InvalidConstantPoolPointerIndexException("Constant pool index is larger than pool size: " + index + " > " + size + ".");
        }
    }

    public static WrongConstantPoolPointerException getInvalidType(ConstantPoolEntry[] pool, int index) throws WrongConstantPoolPointerException {
        return new WrongConstantPoolPointerException("The constant pool entry at " + index + " (" + (pool[index].getClass().getName()) + ") has an invalid type.");
    }

    public static WrongConstantPoolPointerException getInvalidType(ConstantPoolEntry e) throws WrongConstantPoolPointerException {
        return new WrongConstantPoolPointerException("The constant pool entry has an invalid type. (" + e.getClass().getName() + ")");
    }
}
