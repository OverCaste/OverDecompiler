package user.theovercaste.overdecompiler.classdataloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import user.theovercaste.overdecompiler.FileUtilities;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;

import com.google.common.base.CharMatcher;

public class FileClassDataLoader extends BinaryClassDataLoader implements AutoCloseable {
    protected final FileInputStream fileInput;
    protected final File file;

    public FileClassDataLoader(File file) throws FileNotFoundException {
        super(new FileInputStream(file));
        this.file = file;
        fileInput = (FileInputStream) super.input;
    }

    @Override
    public ClassData getClassData( ) throws InvalidClassException, IOException {
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
