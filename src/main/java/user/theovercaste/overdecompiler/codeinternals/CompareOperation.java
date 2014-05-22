package user.theovercaste.overdecompiler.codeinternals;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

public enum CompareOperation {
    GREATER_THAN,
    GREATER_THAN_OR_EQUAL_TO,
    LESS_THAN,
    LESS_THAN_OR_EQUAL_TO,
    EQUAL_TO,
    NOT_EQUAL_TO;

    private static final ImmutableMap<CompareOperation, CompareOperation> oppositeMap = ImmutableMap.<CompareOperation, CompareOperation> builder()
            .put(GREATER_THAN, LESS_THAN_OR_EQUAL_TO)
            .put(GREATER_THAN_OR_EQUAL_TO, LESS_THAN)
            .put(LESS_THAN_OR_EQUAL_TO, GREATER_THAN)
            .put(LESS_THAN, GREATER_THAN_OR_EQUAL_TO)
            .put(EQUAL_TO, NOT_EQUAL_TO)
            .put(NOT_EQUAL_TO, EQUAL_TO)
            .build();

    public static CompareOperation getOpposite(CompareOperation o) {
        Preconditions.checkNotNull(o);
        return oppositeMap.get(o);
    }
}
