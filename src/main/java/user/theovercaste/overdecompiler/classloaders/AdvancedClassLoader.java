package user.theovercaste.overdecompiler.classloaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import user.theovercaste.overdecompiler.classdataloaders.BinaryClassDataLoader;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.ClassParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parsers.ClassParser;
import user.theovercaste.overdecompiler.parsers.JavaParser;

public abstract class AdvancedClassLoader extends ClassLoader {
    private final HashMap<String, Class<?>> loadedClasses = new HashMap<>();

    public AdvancedClassLoader( ) {
        super();
    }

    public AdvancedClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (loadedClasses.containsKey(name)) {
            return loadedClasses.get(name);
        }
        try {
            return super.findSystemClass(name);
        } catch (ClassNotFoundException e) {
            // ignore
        }
        InputStream classData = getResourceAsStream(name.replace('.', '/'));
        if (classData == null) {
            if (getParent() != null) {
                return getParent().loadClass(name);
            }
            return super.loadClass(name);
        }
        try {
            BinaryClassDataLoader loader = new BinaryClassDataLoader(classData);
            ClassData data = loader.getClassData();
            ClassParser parser = new JavaParser(data);
            ParsedClass parsed = parser.parseClass();
            modifyClass(parsed);
            // TODO unparse, convert to binary
        } catch (InvalidClassException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassParsingException e) {
            e.printStackTrace();
        } finally {
            try {
                classData.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new ClassNotFoundException();
    }

    protected abstract void modifyClass(ParsedClass clazz);
}
