package user.theovercaste.overdecompiler.constantpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerIndexException;
import user.theovercaste.overdecompiler.exceptions.WrongConstantPoolPointerTypeException;

import com.google.common.base.Preconditions;

public class ConstantPool {
    private final ArrayList<ConstantPoolEntry> entries;

    private final Map<ConstantPoolEntry, Integer> entryValueMap = new HashMap<>(64);

    public ConstantPool(int size) {
        entries = new ArrayList<>(size);
    }

    private int addValue(ConstantPoolEntry entry) {
        entries.add(entry);
        int index = entries.size() - 1; // it inserted at end
        entryValueMap.put(entry, index);
        return index;
    }

    /**
     * Adds a method to the end of this Constant Pool. This method isn't smart, it won't remove duplicates.
     * 
     * @param entry
     */
    public void add(ConstantPoolEntry entry) {
        entries.add(entry);
    }

    public void set(int index, ConstantPoolEntry entry) {
        entries.set(index, entry);
        entryValueMap.put(entry, index);
    }

    public ConstantPoolEntry get(int index) throws InvalidConstantPoolPointerIndexException {
        if (index < 1) {
            throw new InvalidConstantPoolPointerIndexException("Constant pool index is less than zero: " + index + ".");
        }
        if (index > entries.size()) {
            throw new InvalidConstantPoolPointerIndexException("Constant pool index is larger than pool size: " + index + " > " + entries.size() + ".");
        }
        return entries.get(index - 1); // Start at index 0, pool starts at 1
    }

    public ConstantPoolEntry getUnsafe(int index) {
        return entries.get(index - 1);
    }

    public double getDouble(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryDouble) {
            return ((ConstantPoolEntryDouble) entry).getValue(); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryDouble.class);
    }

    public int addDouble(double value) {
        ConstantPoolEntryDouble entry = new ConstantPoolEntryDouble(value);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public float getFloat(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryFloat) {
            return ((ConstantPoolEntryFloat) entry).getValue(); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryFloat.class);
    }

    public int addFloat(float value) {
        ConstantPoolEntryFloat entry = new ConstantPoolEntryFloat(value);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public long getLong(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryLong) {
            return ((ConstantPoolEntryLong) entry).getValue(); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryLong.class);
    }

    public int addLong(long value) {
        ConstantPoolEntryLong entry = new ConstantPoolEntryLong(value);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public long getInteger(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryInteger) {
            return ((ConstantPoolEntryInteger) entry).getValue(); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryInteger.class);
    }

    public int addInteger(int value) {
        ConstantPoolEntryInteger entry = new ConstantPoolEntryInteger(value);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public String getUtf8(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryUtf8) {
            return ((ConstantPoolEntryUtf8) entry).getValue();
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryUtf8.class);
    }

    public int addUtf8(String string) {
        Preconditions.checkNotNull(string);
        ConstantPoolEntryUtf8 entry = new ConstantPoolEntryUtf8(string);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public String getStringReference(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryString) {
            return getUtf8(((ConstantPoolEntryString) entry).getStringIndex()); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryString.class);
    }

    public int addStringReference(String string) {
        Preconditions.checkNotNull(string);
        int stringIndex = addUtf8(string);
        ConstantPoolEntryString entry = new ConstantPoolEntryString(stringIndex);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    /**
     * Fetches the specified class' name from the constant pool.
     * 
     * @see {@link user.theovercaste.overdecompiler.codeinternals.ClassPath#getInternalPath(String)}
     * 
     * @param index The index from which the class is to be retrieved
     * @return A mangled form of the class.
     * @throws InvalidConstantPoolPointerException
     */
    public String getClassName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryClass) {
            return getUtf8(((ConstantPoolEntryClass) entry).getNameIndex()); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryClass.class);
    }

    public int addClass(String name) {
        int nameIndex = addUtf8(name);
        ConstantPoolEntryClass entry = new ConstantPoolEntryClass(nameIndex);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public String getNameAndTypeName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryNameAndType) {
            return getUtf8(((ConstantPoolEntryNameAndType) entry).nameIndex);
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryNameAndType.class);
    }

    public String getNameAndTypeType(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryNameAndType) {
            return getUtf8(((ConstantPoolEntryNameAndType) entry).descriptorIndex);
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryNameAndType.class);
    }

    public int addNameAndType(String name, String descriptor) {
        int nameIndex = addUtf8(name);
        int descriptorIndex = addUtf8(descriptor);
        ConstantPoolEntryNameAndType entry = new ConstantPoolEntryNameAndType(nameIndex, descriptorIndex);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public int getInvokeDynamicMethodIndex(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryInvokeDynamic) {
            return ((ConstantPoolEntryInvokeDynamic) entry).getMethodAttributeIndex(); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryInvokeDynamic.class);
    }

    public String getInvokeDynamicMethodName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryInvokeDynamic) {
            return getNameAndTypeName(((ConstantPoolEntryInvokeDynamic) entry).getNameAndTypeIndex()); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryInvokeDynamic.class);
    }

    public String getInvokeDynamicMethodType(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryInvokeDynamic) {
            return getNameAndTypeType(((ConstantPoolEntryInvokeDynamic) entry).getNameAndTypeIndex()); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryInvokeDynamic.class);
    }

    public int addInvokeDynamic(int methodIndex, String className, String descriptor) {
        Preconditions.checkNotNull(className);
        Preconditions.checkNotNull(descriptor);
        int nameAndTypeIndex = addNameAndType(className, descriptor);
        ConstantPoolEntryInvokeDynamic entry = new ConstantPoolEntryInvokeDynamic(methodIndex, nameAndTypeIndex);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public String getMethodReferenceClassName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryMethodReference) {
            return getClassName(((ConstantPoolEntryMethodReference) entry).getClassIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryMethodReference.class);
    }

    public String getMethodReferenceName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryMethodReference) {
            return getNameAndTypeName(((ConstantPoolEntryMethodReference) entry).getNameAndTypeIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryMethodReference.class);
    }

    public String getMethodReferenceType(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryMethodReference) {
            return getNameAndTypeType(((ConstantPoolEntryMethodReference) entry).getNameAndTypeIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryMethodReference.class);
    }

    public String getFieldReferenceClassName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryFieldReference) {
            return getClassName(((ConstantPoolEntryFieldReference) entry).getClassIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryFieldReference.class);
    }

    public String getFieldReferenceName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryFieldReference) {
            return getNameAndTypeName(((ConstantPoolEntryFieldReference) entry).getNameAndTypeIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryFieldReference.class);
    }

    public String getFieldReferenceType(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryFieldReference) {
            return getNameAndTypeType(((ConstantPoolEntryFieldReference) entry).getNameAndTypeIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryFieldReference.class);
    }

    public String getInterfaceMethodReferenceClassName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryInterfaceMethodReference) {
            return getClassName(((ConstantPoolEntryInterfaceMethodReference) entry).getClassIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryInterfaceMethodReference.class);
    }

    public String getInterfaceMethodReferenceName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryInterfaceMethodReference) {
            return getNameAndTypeName(((ConstantPoolEntryInterfaceMethodReference) entry).getNameAndTypeIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryInterfaceMethodReference.class);
    }

    public String getInterfaceMethodReferenceType(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryInterfaceMethodReference) {
            return getNameAndTypeType(((ConstantPoolEntryInterfaceMethodReference) entry).getNameAndTypeIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryInterfaceMethodReference.class);
    }

    public String getReferenceClassName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryReference) {
            return getClassName(((ConstantPoolEntryReference) entry).getClassIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryReference.class);
    }

    public String getReferenceName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryReference) {
            return getNameAndTypeName(((ConstantPoolEntryReference) entry).getNameAndTypeIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryReference.class);
    }

    public String getReferenceType(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryReference) {
            return getNameAndTypeType(((ConstantPoolEntryReference) entry).getNameAndTypeIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryReference.class);
    }

    public int addFieldReference(String className, String name, String type) {
        int classIndex = addClass(className);
        int nameAndTypeIndex = addNameAndType(name, type);
        ConstantPoolEntryFieldReference entry = new ConstantPoolEntryFieldReference(classIndex, nameAndTypeIndex);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public int addInterfaceMethodReference(String className, String name, String type) {
        int classIndex = addClass(className);
        int nameAndTypeIndex = addNameAndType(name, type);
        ConstantPoolEntryInterfaceMethodReference entry = new ConstantPoolEntryInterfaceMethodReference(classIndex, nameAndTypeIndex);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public int addMethodReference(String className, String name, String type) {
        int classIndex = addClass(className);
        int nameAndTypeIndex = addNameAndType(name, type);
        ConstantPoolEntryInterfaceMethodReference entry = new ConstantPoolEntryInterfaceMethodReference(classIndex, nameAndTypeIndex);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public long getMethodHandleKind(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryMethodHandle) {
            return ((ConstantPoolEntryMethodHandle) entry).getReferenceKind();
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryMethodHandle.class);
    }

    public long getMethodHandleIndex(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryMethodHandle) {
            return ((ConstantPoolEntryMethodHandle) entry).getReferenceIndex();
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryMethodHandle.class);
    }

    public int addMethodHandle(int methodKind, int methodIndex) {
        ConstantPoolEntryMethodHandle entry = new ConstantPoolEntryMethodHandle(methodKind, methodIndex);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }

    public String getMethodTypeDescriptor(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryMethodType) {
            return getUtf8(((ConstantPoolEntryMethodType) entry).getDescriptorIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryMethodType.class);
    }

    public int addMethodType(String descriptor) {
        int descriptorIndex = addUtf8(descriptor);
        ConstantPoolEntryMethodType entry = new ConstantPoolEntryMethodType(descriptorIndex);
        if (entryValueMap.containsKey(entry)) {
            return entryValueMap.get(entry);
        }
        return addValue(entry);
    }
}
