package user.theovercaste.overdecompiler.parserdata.methodmembers;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodPrintingContext;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class MethodActionSuperConstructor extends MethodAction {
    private ImmutableList<MethodActionPointer> arguments;

    public MethodActionSuperConstructor(ImmutableList<MethodActionPointer> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getStringValue(final ParsedClass c, final ParsedMethod parent, final MethodPrintingContext ctx) {
        return "super(" +
                Joiner.on(", ").join(Iterables.transform(arguments, new Function<MethodActionPointer, String>() {
                    @Override
                    public String apply(MethodActionPointer input) {
                        return input.get(ctx).getStringValue(c, parent, ctx);
                    }
                })) +
                ")";
    }

    public ImmutableList<MethodActionPointer> getArguments( ) {
        return arguments;
    }

    @Override
    public ClassPath getClassType( ) {
        return null;
    }
}
