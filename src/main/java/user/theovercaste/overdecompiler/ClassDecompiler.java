package user.theovercaste.overdecompiler;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import user.theovercaste.overdecompiler.attributes.AttributeData;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntries;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.FieldData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolEntryException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parsers.AbstractParser;
import user.theovercaste.overdecompiler.parsers.AbstractParserFactory;
import user.theovercaste.overdecompiler.printers.AbstractPrinter;
import user.theovercaste.overdecompiler.printers.AbstractPrinterFactory;

import com.google.common.base.CharMatcher;

public class ClassDecompiler {
    public static final int CLASS_MAGIC = 0xCAFEBABE;

    public void decompileFiles(Collection<File> files, AbstractParserFactory parserFactory, AbstractPrinterFactory printerFactory) {
        for (File f : files) {
            decompileFile(f, parserFactory.createParser(), printerFactory.createPrinter());
        }
    }

    public void decompileFile(File decompileFile, AbstractParser parser, AbstractPrinter printer) {
        File toFile = new File(decompileFile.getParent(), FileUtilities.getFileName(decompileFile) + ".java");
        if (toFile.getName().contains("$")) {
            System.out.println("Skipping nested class: " + toFile.getName());
        } else {
            System.out.println("Decompiling: " + toFile.getName());
            try {
                System.out.println(" - Reading binary data...");
                ClassData c = loadClass(decompileFile);
                System.out.println(" - Parsing...");
                ParsedClass parsed = parser.parseClass(c);
                System.out.println(" - Writing...");
                printer.print(parsed, System.out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ClassData loadClass(File file) throws FileNotFoundException, IOException, InvalidClassException {
        try (DataInputStream din = new DataInputStream(new FileInputStream(file))) {
            int magicVersion = din.readInt();
            if (magicVersion != CLASS_MAGIC) {
                throw new InvalidClassException("Invalid class file found: " + file.getAbsolutePath() + " (CLASS_MAGIC not valid: " + Integer.toHexString(magicVersion) + ", expected "
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
            String currentFileName = FileUtilities.getFileName(file);
            CharMatcher dollarSignMatcher = CharMatcher.is('$');
            for (File otherFile : file.getParentFile().listFiles()) {
                String name = otherFile.getName();
                if (name.startsWith(currentFileName) && (name.length() > file.getName().toString().length()) && (name.charAt(currentFileName.length()) == '$') && name.endsWith(".class")) { // Ex: HelloWorld$Inner.class
                    if (((dollarSignMatcher.countIn(name)) - dollarSignMatcher.countIn(currentFileName)) == 1) { // Direct subclass, ex HelloWorld$Inner -> HelloWorld or HelloWorld$Inner$Inner2 -> HelloWorld$Inner
                        ClassData c = loadClass(otherFile);
                        classData.addNestedClass(c);
                    }
                }
            }
            return classData;
        }
    }
}
