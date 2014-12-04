package user.theovercaste.overdecompiler.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

public enum ArithmeticComparison {
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUAL_TO(">="),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUAL_TO("<="),
    EQUAL_TO("=="),
    NOT_EQUAL_TO("!=");

    private static final ImmutableMap<ArithmeticComparison, ArithmeticComparison> oppositeMap = ImmutableMap.<ArithmeticComparison, ArithmeticComparison> builder()
            .put(GREATER_THAN, LESS_THAN_OR_EQUAL_TO)
            .put(GREATER_THAN_OR_EQUAL_TO, LESS_THAN)
            .put(LESS_THAN_OR_EQUAL_TO, GREATER_THAN)
            .put(LESS_THAN, GREATER_THAN_OR_EQUAL_TO)
            .put(EQUAL_TO, NOT_EQUAL_TO)
            .put(NOT_EQUAL_TO, EQUAL_TO)
            .build();

    public static ArithmeticComparison getOpposite(ArithmeticComparison o) {
        Preconditions.checkNotNull(o);
        return oppositeMap.get(o);
    }

    private final String symbol;

    ArithmeticComparison(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol( ) {
        return symbol;
    }
}
