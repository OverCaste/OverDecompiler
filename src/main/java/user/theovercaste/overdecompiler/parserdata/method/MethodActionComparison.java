package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ArithmeticComparison;
import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodPrintingContext;

import com.google.common.base.Preconditions;

public class MethodActionComparison extends MethodActionGetter {
    private final MethodActionPointer left;
    private final ArithmeticComparison operand;
    private final MethodActionPointer right;

    public MethodActionComparison(MethodActionPointer left, ArithmeticComparison operand, MethodActionPointer right) {
        Preconditions.checkNotNull(left, "valueOne");
        Preconditions.checkNotNull(right, "valueTwo");
        Preconditions.checkNotNull(operand, "operand");
        this.left = left;
        this.operand = operand;
        this.right = right;
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return left.get(ctx).getStringValue(c, parent, ctx) + " " + operand.getSymbol() + " " + right.get(ctx).getStringValue(c, parent, ctx);
    }

    @Override
    public ClassPath getClassType( ) {
        return ClassPath.BOOLEAN;
    }
}
