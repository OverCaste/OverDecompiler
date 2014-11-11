package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodPrintingContext;

import com.google.common.base.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class MethodActionInvokeMethod extends MethodAction {
    private final MethodActionPointer invokee;
    private final String method;
    private final ImmutableList<MethodActionPointer> arguments;
    private final ClassPath returnType;

    /**
     * @param invokee the object on which the method is being called. "System.out" in {@code System.out.println("Hello")}
     * @param method the method which is being called.
     * @param arguments the arguments being pased to the method.
     * @param returnType the return type of this method action.
     */
    public MethodActionInvokeMethod(MethodActionPointer invokee, String method, ImmutableList<MethodActionPointer> arguments, ClassPath returnType) {
        Preconditions.checkNotNull(invokee, "invokee");
        Preconditions.checkNotNull(method, "method");
        Preconditions.checkNotNull(arguments, "arguments");
        this.invokee = invokee;
        this.method = method;
        this.arguments = arguments;
        this.returnType = (returnType == null ? ClassPath.VOID : returnType);
    }

    @Override
    public String getStringValue(final ParsedClass c, final ParsedMethod parent, final MethodPrintingContext ctx) {
        return invokee.get(ctx).getStringValue(c, parent, ctx) + "." +
                method + "(" +
                Joiner.on(", ").join(Iterables.transform(arguments, new Function<MethodActionPointer, String>() {
                    @Override
                    public String apply(MethodActionPointer input) {
                        return input.get(ctx).getStringValue(c, parent, ctx);
                    }
                })) +
                ")";
    }

    public String getMethod( ) {
        return method;
    }

    @Override
    public boolean isGetter( ) {
        return !ClassPath.VOID.equals(returnType);
    }

    @Override
    public ClassPath getClassType( ) {
        return returnType;
    }

    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        printingContext.addReference(invokee);
        for(MethodActionPointer p : arguments) {
            printingContext.addReference(p);
        }
    }
    
    @Override
    public String toString( ) {
        return "Invoke method " + invokee + "." + method + "(" + arguments + "), returns: " + returnType;
    }
}
