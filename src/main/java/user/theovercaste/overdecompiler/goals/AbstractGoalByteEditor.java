package user.theovercaste.overdecompiler.goals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import user.theovercaste.overdecompiler.FileUtilities;

public abstract class AbstractGoalByteEditor extends AbstractGoal {
    public void processData(String filePath, boolean recursive) {
        File f = new File(filePath);
        if (!f.exists()) {
            System.err.println("The file to decompile is missing or invalid.");
            return;
        }
        if (f.isDirectory()) {
            System.out.println("Directory detected.");
            processFiles(seekClasses(f, recursive));
        } else {
            String extension = FileUtilities.getFileExtention(f);
            switch (extension) {
                case "jar": {
                    System.out.println("Jar file detected. Extracting...");
                    File dir = new File(f.getParentFile(), FileUtilities.getFileName(f));
                    extractJar(f, dir);
                    processFiles(seekClasses(dir, recursive));
                    break;
                }
                case "class": {
                    System.out.println("Class file detected.");
                    processFiles(Arrays.asList(f));
                    break;
                }
                default:
                    System.err.println("Unknown file type detected: " + extension + ".");
                    break;
            }
        }
    }

    protected abstract void processFiles(Collection<File> f);

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

    private ArrayList<File> seekClasses(File f, boolean recursive) {
        return seekClasses(f, new ArrayList<File>(), recursive);
    }

    private ArrayList<File> seekClasses(File f, ArrayList<File> files, boolean recursive) {
        if (f.isDirectory()) {
            for (File other : f.listFiles()) {
                if (recursive) { // If we're checking recursively... should get on that
                    seekClasses(other, files, recursive);
                } else if ("class".equals(FileUtilities.getFileExtention(other))) {
                    files.add(other);
                }
            }
        }
        else if ("class".equals(FileUtilities.getFileExtention(f))) {
            files.add(f);
        }
        return files;
    }
}
