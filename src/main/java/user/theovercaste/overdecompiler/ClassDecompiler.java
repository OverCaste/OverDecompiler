package user.theovercaste.overdecompiler;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import user.theovercaste.overdecompiler.attributes.Attribute;
import user.theovercaste.overdecompiler.attributes.Attributes;
import user.theovercaste.overdecompiler.codeinternals.Class;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntries;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.datahandlers.AccessFlagHandler;
import user.theovercaste.overdecompiler.datahandlers.ClassBuilder;
import user.theovercaste.overdecompiler.datahandlers.FieldData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.parsers.AbstractParser;

import com.google.common.base.CharMatcher;

public class ClassDecompiler {
	public static final int CLASS_MAGIC = 0xCAFEBABE;

	public void decompileFiles(Collection<File> files, AbstractParser parser) {
		for (File f : files) {
			decompileFile(f, parser);
		}
	}

	public void decompileFile(File decompileFile, AbstractParser parser) {
		File toFile = new File(decompileFile.getParent(), FileUtilities.getFileName(decompileFile) + ".java");
		if (toFile.getName().contains("$")) {
			System.out.println("Skipping nested class: " + toFile.getName());
		} else {
			System.out.println("Decompiling: " + toFile.getName());
			try {
				System.out.println(" - Reading binary data...");
				Class c = loadClass(decompileFile);
				System.out.println(" - Parsing...");
				parser.parseClass(c, System.out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Class loadClass(File file) throws FileNotFoundException, IOException, Exception {
		try (DataInputStream din = new DataInputStream(new FileInputStream(file))) {
			int magicVersion = din.readInt();
			if (magicVersion != CLASS_MAGIC) {
				throw new InvalidClassException("Invalid class file found: " + file.getAbsolutePath() + " (CLASS_MAGIC not valid: " + Integer.toHexString(magicVersion) + ", expected "
						+ Integer.toHexString(CLASS_MAGIC) + ")");
			}
			int minorVersion = din.readUnsignedShort();
			int majorVersion = din.readUnsignedShort();
			System.out.println("Major/minor: " + majorVersion + "." + minorVersion);
			int constantPoolCount = din.readUnsignedShort();
			ConstantPoolEntry[] constantPool = new ConstantPoolEntry[constantPoolCount];
			for (int i = 1; i < constantPoolCount; i++) {
				constantPool[i] = ConstantPoolEntries.readEntry(din);
			}
			AccessFlagHandler accessFlags = new AccessFlagHandler(din.readUnsignedShort());
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
				methods[i] = MethodData.loadMethodInfo(din);
				System.out.println("Return type for " + methods[i].getName(constantPool) + ": " + methods[i].getReturnType(constantPool).getClassName());
			}
			Attribute[] attributes = new Attribute[din.readUnsignedShort()];
			for (int i = 0; i < attributes.length; i++) {
				attributes[i] = Attributes.loadAttribute(din);
			}
			ClassBuilder classBuilder = new ClassBuilder(constantPool);
			classBuilder.setName(thisClassId);
			classBuilder.setParent(superClassId);
			classBuilder.loadFlags(accessFlags);
			for (MethodData m : methods) {
				classBuilder.addMethod(m.toMethod(constantPool));
			}
			for (FieldData f : fields) {
				classBuilder.addField(f.toField(constantPool));
			}
			String currentFileName = FileUtilities.getFileName(file);
			CharMatcher dollarSignMatcher = CharMatcher.is('$');
			for (File otherFile : file.getParentFile().listFiles()) {
				String name = otherFile.getName();
				if (name.startsWith(currentFileName) && (name.length() > file.getName().toString().length()) && (name.charAt(currentFileName.length()) == '$') && name.endsWith(".class")) { // Ex: HelloWorld$Inner.class
					if (((dollarSignMatcher.countIn(name)) - dollarSignMatcher.countIn(currentFileName)) == 1) { // Direct subclass, ex HelloWorld$Inner -> HelloWorld or HelloWorld$Inner$Inner2 -> HelloWorld$Inner
						Class c = loadClass(otherFile);
						classBuilder.addNestedClass(c);
					}
				}
			}
			return classBuilder.build();
		}
	}
}
