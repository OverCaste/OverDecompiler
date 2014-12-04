package user.theovercaste.overdecompiler.parseddata.methodmembers;

import java.util.Arrays;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.collect.Iterables;

public class MethodActionSetVariable extends MethodActionGetter {
    private final int variableIndex;
    private final MethodAction expression;
    private final boolean firstReference;
    private final ClassPath variableType;

    public MethodActionSetVariable(int variableIndex, MethodAction expression, ClassPath variableType, boolean firstReference) {
        this.variableIndex = variableIndex;
        this.expression = expression;
        this.variableType = variableType;
        this.firstReference = firstReference;
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        StringBuilder builder = new StringBuilder();
        if (firstReference) {
            builder.append(variableType.getClassName()).append(" ");
        }
        builder.append(ctx.getVariableName(variableIndex, false, getClassType())).append(" = ").append(expression.getStringValue(c, parent, ctx));
        return builder.toString();
    }

    public int getVariable( ) {
        return variableIndex;
    }

    public MethodAction getExpression( ) {
        return expression;
    }

    public int getVariableIndex( ) {
        return variableIndex;
    }

    public boolean isFirstReference( ) {
        return firstReference;
    }

    public ClassPath getVariableType( ) {
        return variableType;
    }

    @Override
    public ClassPath getClassType( ) {
        return variableType;
    }

    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        expression.countReferences(printingContext);
    }

    @Override
    public String toString( ) {
        return getClass().getName() + " variableIndex at " + variableIndex + " of type " + variableType + " to [" + expression + "]. first reference: " + firstReference;
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Iterables.concat(expression.getImportedElements(), Arrays.asList(variableType));
    }
}
