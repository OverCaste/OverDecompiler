package user.theovercaste.overdecompiler.goals;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.theovercaste.overdecompiler.FileUtilities;

public abstract class AbstractGoalByteEditor extends AbstractGoal {
    private final Logger logger = LoggerFactory.getLogger(AbstractGoalByteEditor.class);

    public void processData(String filePath, boolean recursive) {
        File f = new File(filePath);
        if (!f.exists()) {
            logger.error("The file at path {} does not exist.", f.getAbsolutePath());
            return;
        }
        if (f.isDirectory()) {
            logger.debug("Directory detected.");
            processFiles(seekClasses(f, recursive));
        } else {
            String extension = FileUtilities.getFileExtention(f);
            switch (extension) {
                case "jar": {
                    logger.info("JAR file detected. Extracting...");
                    File dir = new File(f.getParentFile(), FileUtilities.getFileName(f));
                    extractJar(f, dir);
                    processFiles(seekClasses(dir, recursive));
                    break;
                }
                case "class": {
                    logger.debug("Binary class file detected.");
                    processFiles(Arrays.asList(f));
                    break;
                }
                default:
                    logger.error("Unrecognized file type detected: {}.", extension);
                    break;
            }
        }
    }

    protected abstract void processFiles(Collection<File> f);

    private void extractJar(File jar, File dir) {
        if (!dir.exists()) {
            if (dir.mkdir()) {
                logger.info("Created output directory: {}", dir.getAbsolutePath());
            } else {
                logger.error("Failed to create output directory: {}", dir.getAbsolutePath());
                return;
            }
        } else {
            logger.info("Using output directory: {}", dir.getAbsolutePath());
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
                } catch (IOException ex) {
                    logger.error("Failed to decompress a part of jar file {}.", jar.getPath(), ex);
                }
            }
            zin.close();
        } catch (FileNotFoundException e) {
            logger.error("The jar file being decompressed was deleted before it could be read from.", e);
        } catch (IOException e) {
            logger.error("Failed to read from the jar file to be decompressed, or create a new folder for the output.", e);
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
