package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class MethodActionInvokeMethodStatic extends MethodActionGetter {
    private ClassPath path;
    private String method;
    private ImmutableList<MethodAction> arguments;

    public MethodActionInvokeMethodStatic(ClassPath path, String method, ImmutableList<MethodAction> arguments) {
        this.path = path;
        this.method = method;
        this.arguments = arguments;
    }

    @Override
    public String getStringValue(final ParsedClass c, final ParsedMethod parent) {
        StringBuilder builder = new StringBuilder();
        if (!(path.equalsType(c.getClassPath()) || ((path.getClassPackage().length() == 0) && path.getClassName().equals(c.getClassPath().getClassName())))) {
            builder.append(path.getClassName()).append(".");
        }
        builder.append(method);
        builder.append("(");
        builder.append(Joiner.on(", ").join(Iterables.transform(arguments, new Function<MethodAction, String>() {
            @Override
            public String apply(MethodAction input) {
                return input.getStringValue(c, parent);
            }
        })));
        builder.append(")");
        return builder.toString();
    }
}
