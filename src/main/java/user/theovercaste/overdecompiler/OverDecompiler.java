package user.theovercaste.overdecompiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class OverDecompiler {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("usage: overdecompiler [file]");
			return;
		}
		OverDecompiler d = new OverDecompiler();
		d.decompile(args[0]);
	}

	public void decompile(String filePath) {
		File f = new File(filePath);
		if (!f.exists()) {
			System.err.println("The file to decompile is missing or invalid.");
			return;
		}
		ClassDecompiler decompiler = new ClassDecompiler();
		if (f.isDirectory()) {
			System.out.println("Directory detected. Decompiling...");
			decompiler.decompileFiles(seekClasses(f));
		} else {
			String extension = FileUtilities.getFileExtention(f);
			switch (extension) {
				case "jar": {
					System.out.println("Jar file detected. Extracting...");
					File dir = new File(f.getParentFile(), FileUtilities.getFileName(f));
					System.out.println("Decompiling...");
					extractJar(f, dir);
					decompiler.decompileFiles(seekClasses(dir));
					break;
				}
				case "class": {
					System.out.println("Class file detected... Decompiling.");
					decompiler.decompileFiles(Arrays.asList(f));
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
