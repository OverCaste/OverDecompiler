package user.theovercaste.overdecompiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import user.theovercaste.overdecompiler.exceptions.InvalidArgumentException;
import user.theovercaste.overdecompiler.parsers.AbstractParserFactory;
import user.theovercaste.overdecompiler.parsers.JavaParserFactory;
import user.theovercaste.overdecompiler.printers.AbstractPrinterFactory;
import user.theovercaste.overdecompiler.printers.PrettyPrinterFactory;

public class OverDecompiler {
	public static void main(String[] args) {
		try {
			if (args.length < 1) {
				sendUsageMessage();
				return;
			}
			OverDecompiler d = new OverDecompiler();
			AbstractParserFactory parserFactory = getParserFactory(args);
			AbstractPrinterFactory printerFactory = getPrinterFactory(args);
			d.decompile(args[args.length - 1], parserFactory, printerFactory);
		} catch (InvalidArgumentException e) {
			System.out.println(e.getMessage());
			sendUsageMessage();
		}
	}

	public static void sendUsageMessage( ) {
		System.out.println("Usage: overdecompiler (options) [file]");
		System.out.println("where possible options include:");
		System.out.println("  -help  --help  -?\t\tPrint this usage message");
		System.out.println("  -parser (class)\t\tSpecify a parser to be used.");
		System.out.println("  -printer (class)\t\tSpecify the printer to be used.");
	}

	public static AbstractParserFactory getParserFactory(String[] args) throws InvalidArgumentException {
		for (int i = 0; i < args.length; i++) {
			String s = args[i];
			if ("-parser".equalsIgnoreCase(s) || "--parser".equalsIgnoreCase(s)) {
				if ((i + 1) >= args.length) {
					throw new InvalidArgumentException("The -parser must not be at the end of the arguments.");
				}
				String parserName = args[i + 1];
				if (!parserName.contains(".")) {
					parserName = "user.theovercaste.overdecompiler.parsers." + parserName;
				}
				try {
					Class<?> clazz = Class.forName(parserName);
					if (clazz.isAssignableFrom(AbstractParserFactory.class)) {
						Method getInstanceMethod = clazz.getMethod("getInstance");
						return (AbstractParserFactory) getInstanceMethod.invoke(null);
					}
				} catch (ClassNotFoundException e) { // Reflection gives you a lot of errors.
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				throw new InvalidArgumentException("The -parser flag's value isn't a valid JavaParser.");
			}
		}
		return JavaParserFactory.getInstance();
	}

	public static AbstractPrinterFactory getPrinterFactory(String[] args) throws InvalidArgumentException {
		for (int i = 0; i < args.length; i++) {
			String s = args[i];
			if ("-printer".equalsIgnoreCase(s) || "--printer".equalsIgnoreCase(s)) {
				if ((i + 1) >= args.length) {
					throw new InvalidArgumentException("The -printer must not be at the end of the arguments.");
				}
				String parserName = args[i + 1];
				if (!parserName.contains(".")) {
					parserName = "user.theovercaste.overdecompiler.printers." + parserName;
				}
				try {
					Class<?> clazz = Class.forName(parserName);
					if (clazz.isAssignableFrom(AbstractPrinterFactory.class)) {
						Method getInstanceMethod = clazz.getMethod("getInstance");
						return (AbstractPrinterFactory) getInstanceMethod.invoke(null);
					}
				} catch (ClassNotFoundException e) { // Reflection gives you a lot of errors.
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
				throw new InvalidArgumentException("The -parser flag's value isn't a valid JavaParser.");
			}
		}
		return PrettyPrinterFactory.getInstance();
	}

	public void decompile(String filePath, AbstractParserFactory parserFactory, AbstractPrinterFactory printerFactory) {
		File f = new File(filePath);
		if (!f.exists()) {
			System.err.println("The file to decompile is missing or invalid.");
			return;
		}
		ClassDecompiler decompiler = new ClassDecompiler();
		if (f.isDirectory()) {
			System.out.println("Directory detected. Decompiling...");
			decompiler.decompileFiles(seekClasses(f), parserFactory, printerFactory);
		} else {
			String extension = FileUtilities.getFileExtention(f);
			switch (extension) {
				case "jar": {
					System.out.println("Jar file detected. Extracting...");
					File dir = new File(f.getParentFile(), FileUtilities.getFileName(f));
					System.out.println("Decompiling...");
					extractJar(f, dir);
					decompiler.decompileFiles(seekClasses(dir), parserFactory, printerFactory);
					break;
				}
				case "class": {
					System.out.println("Class file detected... Decompiling.");
					decompiler.decompileFiles(Arrays.asList(f), parserFactory, printerFactory);
					break;
				}
				default:
					System.err.println("Unknown file type detected: " + extension + ".");
					break;
			}
		}
	}

	private void extractJar(File jar, File dir) {
		if (!dir.exists()) {
			if (dir.mkdir()) {
				System.out.println("Made export directory: " + dir.getAbsolutePath());
			} else {
				System.err.println("Failed to make export directory: " + dir.getAbsolutePath());
				return;
			}
		} else {
			System.out.println("Exporting to directory: " + dir.getAbsolutePath());
		}

		byte[] buffer = new byte[2048];

		try {
			ZipInputStream zin = new ZipInputStream(new FileInputStream(jar));
			for (ZipEntry e = zin.getNextEntry(); e != null; e = zin.getNextEntry()) {
				File out = new File(dir, e.getName());
				out.getParentFile().mkdirs();
				out.createNewFile();
				try (FileOutputStream fout = new FileOutputStream(out)) {
					int length = 0;
					while ((length = zin.read(buffer)) > 0) {
						fout.write(buffer, 0, length);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			zin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<File> seekClasses(File f) {
		return seekClasses(f, new ArrayList<File>());
	}

	private ArrayList<File> seekClasses(File f, ArrayList<File> files) {
		if (f.isDirectory()) {
			for (File other : f.listFiles()) {
				seekClasses(other, files);
			}
		}
		else if ("class".equals(FileUtilities.getFileExtention(f))) {
			files.add(f);
		}
		return files;
	}
}
