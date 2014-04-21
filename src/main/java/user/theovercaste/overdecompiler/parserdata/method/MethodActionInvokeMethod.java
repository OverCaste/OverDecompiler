package user.theovercaste.overdecompiler.parserdata.method;

import java.util.Arrays;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class MethodActionInvokeMethod implements MethodActionGetter {
	private MethodActionGetter invokee;
	private String method;
	private MethodAction[] arguments;

	public MethodActionInvokeMethod(MethodActionGetter invokee, String method, MethodAction... arguments) {
		this.invokee = invokee;
		this.method = method;
		this.arguments = arguments;
	}

	@Override
	public String getStringValue(final ParsedClass c, final ParsedMethod parent) {
		return invokee.getStringValue(c, parent) + "." +
				method + "(" +
				Joiner.on(", ").join(Iterables.transform(Arrays.asList(arguments), new Function<MethodAction, String>() {
					@Override
					public String apply(MethodAction input) {
						return input.getStringValue(c, parent);
					}
				})) +
				")";
	}
}
