package user.theovercaste.overdecompiler.classloaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import user.theovercaste.overdecompiler.classdataloaders.BinaryClassDataLoader;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InvalidClassException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parsers.AbstractParser;
import user.theovercaste.overdecompiler.parsers.JavaParser;

public abstract class AdvancedClassLoader extends ClassLoader {
    private final HashMap<String, Class<?>> loadedClasses = new HashMap<>();
    private final AbstractParser parser = JavaParser.Factory.getInstance().createParser();

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
            ParsedClass parsed = parser.parseClass(data);
            modifyClass(parsed);
        } catch (InvalidClassException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConstantPoolPointerException e) {
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
