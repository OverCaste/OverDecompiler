package user.theovercaste.overdecompiler.parseddata.methodmembers;

import java.util.Arrays;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.Preconditions;

public class MethodActionGetConstantClass extends MethodActionGetter {
    private final ClassPath classPath;

    public MethodActionGetConstantClass(ClassPath classPath) {
        this.classPath = Preconditions.checkNotNull(classPath);
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return classPath.getClassName() + ".class";
    }

    @Override
    public ClassPath getClassType( ) {
        return ClassPath.OBJECT_CLASS;
    }

    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        // Do nothing
    }

    @Override
    public boolean isForceInlined( ) {
        return true;
    }

    @Override
    public String toString( ) {
        return "load constant class " + getStringValue(null, null, null);
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Arrays.asList(classPath);
    }
}
