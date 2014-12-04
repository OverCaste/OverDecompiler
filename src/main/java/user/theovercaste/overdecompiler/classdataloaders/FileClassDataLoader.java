package user.theovercaste.overdecompiler.classdataloaders;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.theovercaste.overdecompiler.FileUtilities;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;

import com.google.common.base.CharMatcher;

public class FileClassDataLoader extends BinaryClassDataLoader implements AutoCloseable {
    private final Logger logger = LoggerFactory.getLogger(FileClassDataLoader.class);

    protected final FileInputStream fileInput;
    protected final File file;

    public FileClassDataLoader(File file) throws FileNotFoundException {
        super(new FileInputStream(file));
        this.file = file;
        fileInput = (FileInputStream) super.input;
    }

    @Override
    public ClassData getClassData( ) throws InvalidClassException {
        ClassData ret = super.getClassData();
        String currentFileName = FileUtilities.getFileName(file);
        CharMatcher dollarSignMatcher = CharMatcher.is('$');
        for (File otherFile : file.getParentFile().listFiles()) {
            String name = otherFile.getName();
            if (name.startsWith(currentFileName) && (name.length() > file.getName().toString().length()) && (name.charAt(currentFileName.length()) == '$') && name.endsWith(".class")) { // Ex: HelloWorld$Inner.class
                if (((dollarSignMatcher.countIn(name)) - dollarSignMatcher.countIn(currentFileName)) == 1) { // Direct subclass, ex HelloWorld$Inner -> HelloWorld or HelloWorld$Inner$Inner2 -> HelloWorld$Inner
                    try (FileClassDataLoader innerLoader = new FileClassDataLoader(otherFile)) {
                        ClassData innerData = innerLoader.getClassData();
                        ret.addNestedClass(innerData);
                    } catch (FileNotFoundException ex) {
                        logger.warn("A subclass wasn't available for decompilation.", ex);
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        logger.warn("Failed to close output stream for inner class data loader.", ex);
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public void close( ) throws IOException {
        fileInput.close();
    }
}
