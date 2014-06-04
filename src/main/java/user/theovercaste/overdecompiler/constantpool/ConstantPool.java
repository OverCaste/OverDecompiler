package user.theovercaste.overdecompiler.constantpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import user.theovercaste.overdecompiler.constantpool.datacontainers.ClassContainer;
import user.theovercaste.overdecompiler.constantpool.datacontainers.FieldReferenceContainer;
import user.theovercaste.overdecompiler.constantpool.datacontainers.InterfaceMethodReferenceContainer;
import user.theovercaste.overdecompiler.constantpool.datacontainers.MethodReferenceContainer;
import user.theovercaste.overdecompiler.constantpool.datacontainers.NameAndTypeContainer;
import user.theovercaste.overdecompiler.constantpool.datacontainers.StringReferenceContainer;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerIndexException;
import user.theovercaste.overdecompiler.exceptions.WrongConstantPoolPointerTypeException;

import com.google.common.base.Preconditions;

public class ConstantPool {
    private final ArrayList<ConstantPoolEntry> entries;

    private final Map<Object, Integer> entryValueMap = new HashMap<>(64);

    public ConstantPool(int size) {
        entries = new ArrayList<>(size);
    }

    private int addValue(ConstantPoolEntry entry, Object container) {
        entries.add(entry);
        int index = entries.size() - 1; // it inserted at end
        entryValueMap.put(container, index);
        return index;
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

    public double getDouble(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryDouble) {
            return ((ConstantPoolEntryDouble) entry).getValue(); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryDouble.class);
    }

    public int addDouble(double value) {
        if (entryValueMap.containsKey(value)) {
            return entryValueMap.get(value);
        }
        ConstantPoolEntryDouble entry = new ConstantPoolEntryDouble(value);
        return addValue(entry, value);
    }

    public float getFloat(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryFloat) {
            return ((ConstantPoolEntryFloat) entry).getValue(); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryFloat.class);
    }

    public int addFloat(float value) {
        if (entryValueMap.containsKey(value)) {
            return entryValueMap.get(value);
        }
        ConstantPoolEntryFloat entry = new ConstantPoolEntryFloat(value);
        return addValue(entry, value);
    }

    public long getLong(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryLong) {
            return ((ConstantPoolEntryLong) entry).getValue(); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryLong.class);
    }

    public int addLong(long value) {
        if (entryValueMap.containsKey(value)) {
            return entryValueMap.get(value);
        }
        ConstantPoolEntryLong entry = new ConstantPoolEntryLong(value);
        return addValue(entry, value);
    }

    public long getInteger(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryInteger) {
            return ((ConstantPoolEntryInteger) entry).getValue(); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryInteger.class);
    }

    public int addInteger(int value) {
        if (entryValueMap.containsKey(value)) {
            return entryValueMap.get(value);
        }
        ConstantPoolEntryInteger entry = new ConstantPoolEntryInteger(value);
        return addValue(entry, value);
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
        if (entryValueMap.containsKey(string)) {
            return entryValueMap.get(string);
        }
        ConstantPoolEntryUtf8 entry = new ConstantPoolEntryUtf8(string);
        return addValue(entry, string);
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
        StringReferenceContainer container = new StringReferenceContainer(stringIndex);
        if (entryValueMap.containsKey(container)) {
            return entryValueMap.get(string);
        }
        ConstantPoolEntryString entry = new ConstantPoolEntryString(stringIndex);
        return addValue(entry, string);
    }

    public String getClassName(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryClass) {
            return getUtf8(((ConstantPoolEntryClass) entry).getNameIndex()); // Get the string this 'pointer' is pointing to.
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryClass.class);
    }

    public int addClass(String name) {
        int nameIndex = addUtf8(name);
        ClassContainer container = new ClassContainer(nameIndex);
        if (entryValueMap.containsKey(container)) {
            return entryValueMap.get(container);
        }
        ConstantPoolEntryClass entry = new ConstantPoolEntryClass(nameIndex);
        return addValue(entry, name);
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
        NameAndTypeContainer container = new NameAndTypeContainer(nameIndex, descriptorIndex);
        if (entryValueMap.containsKey(container)) {
            return entryValueMap.get(container);
        }
        ConstantPoolEntryNameAndType entry = new ConstantPoolEntryNameAndType(nameIndex, descriptorIndex);
        return addValue(entry, container);
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
        ConstantPoolEntryInvokeDynamic container = new ConstantPoolEntryInvokeDynamic(methodIndex, nameAndTypeIndex);
        if (entryValueMap.containsKey(container)) {
            return entryValueMap.get(container);
        }
        ConstantPoolEntryInvokeDynamic entry = new ConstantPoolEntryInvokeDynamic(methodIndex, nameAndTypeIndex);
        return addValue(entry, container);
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
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryNameAndType.class);
    }

    public String getReferenceType(int index) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry entry = get(index);
        if (entry instanceof ConstantPoolEntryReference) {
            return getNameAndTypeType(((ConstantPoolEntryReference) entry).getNameAndTypeIndex());
        }
        throw WrongConstantPoolPointerTypeException.constructException(index, this, ConstantPoolEntryNameAndType.class);
    }

    public int addFieldReference(String className, String name, String type) {
        int classIndex = addClass(className);
        int nameAndTypeIndex = addNameAndType(name, type);
        FieldReferenceContainer container = new FieldReferenceContainer(classIndex, nameAndTypeIndex);
        if (entryValueMap.containsKey(container)) {
            return entryValueMap.get(container);
        }
        ConstantPoolEntryFieldReference entry = new ConstantPoolEntryFieldReference(classIndex, nameAndTypeIndex);
        return addValue(entry, container);
    }

    public int addInterfaceMethodReference(String className, String name, String type) {
        int classIndex = addClass(className);
        int nameAndTypeIndex = addNameAndType(name, type);
        InterfaceMethodReferenceContainer container = new InterfaceMethodReferenceContainer(classIndex, nameAndTypeIndex);
        if (entryValueMap.containsKey(container)) {
            return entryValueMap.get(container);
        }
        ConstantPoolEntryInterfaceMethodReference entry = new ConstantPoolEntryInterfaceMethodReference(classIndex, nameAndTypeIndex);
        return addValue(entry, container);
    }

    public int addMethodReference(String className, String name, String type) {
        int classIndex = addClass(className);
        int nameAndTypeIndex = addNameAndType(name, type);
        MethodReferenceContainer container = new MethodReferenceContainer(classIndex, nameAndTypeIndex);
        if (entryValueMap.containsKey(container)) {
            return entryValueMap.get(container);
        }
        ConstantPoolEntryInterfaceMethodReference entry = new ConstantPoolEntryInterfaceMethodReference(classIndex, nameAndTypeIndex);
        return addValue(entry, container);
    }

    public ConstantPoolEntry getUnsafe(int index) {
        return entries.get(index - 1);
    }
}
