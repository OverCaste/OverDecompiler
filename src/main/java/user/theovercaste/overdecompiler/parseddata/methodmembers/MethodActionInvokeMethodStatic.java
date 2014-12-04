package user.theovercaste.overdecompiler.parseddata.methodmembers;

import java.util.Arrays;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class MethodActionInvokeMethodStatic extends MethodActionGetter {
    private final ClassPath path;
    private final ClassPath returnType;
    private final String method;
    private final ImmutableList<MethodActionPointer> arguments;

    public MethodActionInvokeMethodStatic(ClassPath path, ClassPath returnType, String method, ImmutableList<MethodActionPointer> arguments) {
        this.path = path;
        this.returnType = returnType;
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Arrays.asList(path);
    }

    @Override
    public ClassPath getClassType( ) {
        return returnType;
    }

    @Override
    public String getStringValue(final ParsedClass c, final ParsedMethod parent, final MethodPrintingContext ctx) {
        StringBuilder builder = new StringBuilder();
        if (!(path.equalsType(c.getClassPath()) || ((path.getClassPackage().length() == 0) && path.getClassName().equals(c.getClassPath().getClassName())))) {
            builder.append(path.getClassName()).append(".");
        }
        builder.append(method);
        builder.append("(");
        builder.append(Joiner.on(", ").join(Iterables.transform(arguments, new Function<MethodActionPointer, String>() {
            @Override
            public String apply(MethodActionPointer input) {
                return input.get(ctx).getStringValue(c, parent, ctx);
            }
        })));
        builder.append(")");
        return builder.toString();
    }

    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        // TODO Auto-generated method stub
    }
}
