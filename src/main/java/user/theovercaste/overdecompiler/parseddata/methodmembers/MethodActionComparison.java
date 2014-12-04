package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ArithmeticComparison;
import user.theovercaste.overdecompiler.util.ClassPath;

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
