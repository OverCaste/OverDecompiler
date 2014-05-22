package user.theovercaste.overdecompiler.codeinternals;

public enum CodeBlockType {
    /**
     * Internally
     * 
     * <pre>
     * if (var != null) {
     *     int a = 5;
     * }
     * </pre>
     * 
     * Compiles to
     * 
     * <pre>
     * 0: ifnull [var] -> 2
     * 1: //define a
     * 2: //continue execution
     * </pre>
     */
    IF,
    /**
     * Internally
     * 
     * <pre>
     * do {
     *     int a = 5;
     * } while (var != null);
     * </pre>
     * 
     * Compiles to
     * 
     * <pre>
     * 0: //define a
     * 0: ifnonnull [var] -> 0
     * </pre>
     */
    DO_WHILE,
    /**
     * Internally
     * 
     * <pre>
     * while (var != null) {
     *     int a = 5;
     * }
     * </pre>
     * 
     * Compiles to
     * 
     * <pre>
     * 0: ifnull [var] -> 3
     * 1: //define a
     * 2: goto 0
     * 3: //continue execution
     * </pre>
     */
    WHILE,
    FOR, // TODO descriptions
    SWITCH;
}
