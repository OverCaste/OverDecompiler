package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.util.ClassPath;
import user.theovercaste.overdecompiler.util.ClassPaths;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class MethodActionNewObject extends MethodActionGetter {
    private ClassPath classType;
    private ImmutableList<ClassPath> arguments;

    public MethodActionNewObject(ClassPath classType, ImmutableList<ClassPath> arguments) {
        Preconditions.checkNotNull(classType);
        Preconditions.checkNotNull(arguments);
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent) {
        return "new " + classType.getClassName() + "(" + (arguments.isEmpty() ? "" : Joiner.on(", ").join(ClassPaths.transformDefinitions(arguments)) + ")");
    }

    public ClassPath getClassType( ) {
        return classType;
    }

    public ImmutableList<ClassPath> getArguments( ) {
        return arguments;
    }
}
