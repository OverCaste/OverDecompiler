package user.theovercaste.overdecompiler.classdataloaders;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntries;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolEntryException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedField;

public class BinaryClassDataLoader implements ClassDataLoader {
    public static final int CLASS_MAGIC = 0xCAFEBABE;

    protected final InputStream input;

    public BinaryClassDataLoader(InputStream input) {
        this.input = input;
    }

    @Override
    public ParsedClass getClassData( ) throws InvalidClassException, IOException {
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
                constantPool.set(i, ConstantPoolEntries.readEntry(din));
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
            fields[i] = FieldData.loadFieldInfo(din);
        }
        MethodData[] methods = new MethodData[din.readUnsignedShort()];
        for (int i = 0; i < methods.length; i++) {
            methods[i] = MethodData.loadMethodData(din);
        }
        AttributeData[] attributes = new AttributeData[din.readUnsignedShort()];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = AttributeData.loadAttribute(din);
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

    private ParsedField parseField(ConstantPool pool, DataInputStream din) {
        int flags = din.readUnsignedShort();
        int nameIndex = din.readUnsignedShort();
        int descriptorIndex = din.readUnsignedShort();
        int attributeCount = din.readUnsignedShort();
        String name = pool.get
        ParsedField ret = new ParsedField()
    }
}
