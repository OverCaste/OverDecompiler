package user.theovercaste.overdecompiler.constantpool;

import java.util.ArrayList;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerIndexException;

public class ConstantPool {
    private final ArrayList<ConstantPoolEntry> entries;

    public ConstantPool(int size) {
        entries = new ArrayList<>(size);
    }

    public ConstantPoolEntry get(int index) throws InvalidConstantPoolPointerIndexException {
        if (index < 0) {
            throw new InvalidConstantPoolPointerIndexException("Constant pool index is less than zero: " + index + ".");
        }
        if (index >= entries.size()) {
            throw new InvalidConstantPoolPointerIndexException("Constant pool index is larger than pool size: " + index + " > " + entries.size() + ".");
        }
        return entries.get(index - 1); // Start at index 0, pool starts at 1
    }

    public ConstantPoolEntry getUnsafe(int index) {
        return entries.get(index - 1);
    }

    public void set(int index, ConstantPoolEntry e) {
        entries.add(index - 1, e);
    }
}
