package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class MethodActionSuperConstructor extends MethodActionGetter {
    private ImmutableList<MethodAction> arguments;

    public MethodActionSuperConstructor(ImmutableList<MethodAction> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getStringValue(final ParsedClass c, final ParsedMethod parent) {
        return "super(" +
                Joiner.on(", ").join(Iterables.transform(arguments, new Function<MethodAction, String>() {
                    @Override
                    public String apply(MethodAction input) {
                        return input.getStringValue(c, parent);
                    }
                })) +
                ")";
    }

    public ImmutableList<MethodAction> getArguments( ) {
        return arguments;
    }
}
