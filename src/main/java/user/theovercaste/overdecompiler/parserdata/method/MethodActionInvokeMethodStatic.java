package user.theovercaste.overdecompiler.parserdata.method;

import java.util.Arrays;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class MethodActionInvokeMethodStatic extends MethodActionGetter {
	private ClassPath path;
	private String method;
	private MethodAction[] arguments;

	public MethodActionInvokeMethodStatic(int lineNumber, ClassPath path, String method, MethodAction... arguments) {
		super(lineNumber);
		this.path = path;
		this.method = method;
		this.arguments = arguments;
	}

	@Override
	public String getStringValue(final ParsedClass c, final ParsedMethod parent) {
		StringBuilder builder = new StringBuilder();
		if (!(path.equalsType(c.getClassPath()) || ((path.getClassPackage().length() == 0) && path.getClassName().equals(c.getClassPath().getClassName())))) {
			c.addImport(path);
			builder.append(path.getClassName()).append(".");
		}
		builder.append(method);
		builder.append("(");
		builder.append(Joiner.on(", ").join(Iterables.transform(Arrays.asList(arguments), new Function<MethodAction, String>() {
			@Override
			public String apply(MethodAction input) {
				return input.getStringValue(c, parent);
			}
		})));
		builder.append(")");
		return builder.toString();
	}
}
