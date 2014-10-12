package user.theovercaste.overdecompiler.parserdata.method;

import user.theovercaste.overdecompiler.codeinternals.ArithmeticComparison;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

import com.google.common.base.Preconditions;

public class MethodActionComparison extends MethodActionGetter {
    private final MethodActionGetter left;
    private final ArithmeticComparison operand;
    private final MethodActionGetter right;
    
    public MethodActionComparison(MethodActionGetter valueOne, ArithmeticComparison operand, MethodActionGetter valueTwo) {
        Preconditions.checkNotNull(valueOne, "valueOne");
        Preconditions.checkNotNull(valueTwo, "valueTwo");
        Preconditions.checkNotNull(operand, "operand");
        this.left = valueOne;
        this.operand = operand;
        this.right = valueTwo;
    }
    
    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent) {
        return left.getStringValue(c, parent) + " " + operand.getSymbol() + " " + right.getStringValue(c, parent);
    }
}
