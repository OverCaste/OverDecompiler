package user.theovercaste.overdecompiler.classdataloaders;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntries;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.FieldData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolEntryException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class BinaryClassDataLoader implements ClassDataLoader {
    public static final int CLASS_MAGIC = 0xCAFEBABE;

    protected final InputStream input;

    public BinaryClassDataLoader(InputStream input) {
        this.input = input;
    }

    @Override
    public ClassData getClassData( ) throws InvalidClassException, IOException {
        DataInputStream din = new DataInputStream(input);
        int magicVersion = din.readInt();
        if (magicVersion != CLASS_MAGIC) {
            throw new InvalidClassException("Invalid class file. (CLASS_MAGIC not valid: " + Integer.toHexString(magicVersion) + ", expected "
                    + Integer.toHexString(CLASS_MAGIC) + ")");
        }
        int minorVersion = din.readUnsignedShort();
        int majorVersion = din.readUnsignedShort();
        if ((majorVersion != 52) || (minorVersion != 0)) {
            System.out.println("Warning: This class file's version is " + majorVersion + "." + minorVersion + ". OverDecompiler is only tested with classes of version 52.0");
        }
        int constantPoolCount = din.readUnsignedShort();
        ConstantPool constantPool = new ConstantPool(constantPoolCount);
        for (int i = 1; i < constantPoolCount; i++) {
            try {
                constantPool.add(ConstantPoolEntries.readEntry(din));
            } catch (InvalidConstantPoolEntryException e) {
                System.out.println("Warning: invalid constant pool entry found at index " + i + ".");
            }
        }
        int classFlags = din.readUnsignedShort();
        int thisClassId = din.readUnsignedShort();
        int superClassId = din.readUnsignedShort();

        int[] interfaces = new int[din.readUnsignedShort()];
        for (int i = 0; i < interfaces.length; i++) {
            interfaces[i] = din.readUnsignedShort();
        }
        FieldData[] fields = new FieldData[din.readUnsignedShort()];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = FieldData.loadField(din);
        }
        MethodData[] methods = new MethodData[din.readUnsignedShort()];
        for (int i = 0; i < methods.length; i++) {
            methods[i] = MethodData.loadMethod(din);
        }
        AttributeData[] attributes = new AttributeData[din.readUnsignedShort()];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = AttributeData.loadAttribute(din);
            try {
                System.out.println("Attribute: " + attributes[i].getName(constantPool));
            } catch (InvalidConstantPoolPointerException e) {
                e.printStackTrace();
            }
        }
        ClassData classData = new ClassData(constantPool);
        classData.setClassId(thisClassId);
        classData.setParentId(superClassId);
        classData.setFlags(classFlags);
        for (MethodData m : methods) {
            classData.addMethod(m);
        }
        for (FieldData f : fields) {
            classData.addField(f);
        }
        for (int i : interfaces) {
            classData.addInterface(i);
        }
        return classData;
    }
}
