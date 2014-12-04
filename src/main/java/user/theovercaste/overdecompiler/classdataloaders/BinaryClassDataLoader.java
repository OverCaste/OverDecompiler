package user.theovercaste.overdecompiler.classdataloaders;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntries;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolEntryException;
import user.theovercaste.overdecompiler.rawclassdata.*;
import user.theovercaste.overdecompiler.util.AttributeData;

public class BinaryClassDataLoader implements ClassDataLoader {
    public static final int CLASS_MAGIC = 0xCAFEBABE;
    private static final Logger logger = LoggerFactory.getLogger(BinaryClassDataLoader.class);

    protected final InputStream input;

    public BinaryClassDataLoader(InputStream input) {
        this.input = input;
    }

    @Override
    public ClassData getClassData( ) throws InvalidClassException {
        try {
            DataInputStream din = new DataInputStream(input);
            int magicVersion = din.readInt();
            if (magicVersion != CLASS_MAGIC) {
                throw new InvalidClassException("Invalid class file. (CLASS_MAGIC not valid: " + Integer.toHexString(magicVersion) + ", expected "
                        + Integer.toHexString(CLASS_MAGIC) + ")");
            }
            int minorVersion = din.readUnsignedShort();
            int majorVersion = din.readUnsignedShort();
            int constantPoolCount = din.readUnsignedShort();
            ConstantPool constantPool = new ConstantPool(constantPoolCount);
            for (int i = 1; i < constantPoolCount; i++) {
                try {
                    constantPool.add(ConstantPoolEntries.readEntry(din));
                } catch (InvalidConstantPoolEntryException e) {
                    logger.warn("Invalid constant pool entry found at index {}.", i, e);
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
            }
            ClassData classData = new ClassData(majorVersion, minorVersion, constantPool);
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
            for (AttributeData a : attributes) {
                classData.addAttribute(a);
            }
            return classData;
        } catch (EOFException ex) {
            throw new InvalidClassException("Unexpectedly reached the end of the binary data in the class file.", ex);
        } catch (IOException ex) {
            throw new InvalidClassException("An IOException was thrown while reading the class file.", ex);
        }
    }
}
