package user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers;

import java.util.*;

import user.theovercaste.overdecompiler.util.ClassPath;

public class MethodDecompileContext {
    private final Stack<MethodActionPointer> actionPointers = new Stack<>();
    private final Map<Integer, MethodActionPointer> variableMap = new HashMap<>();
    private final List<MethodActionParameterPointer> parameters = new ArrayList<>();

    /** If this method isn't static, it has a this. object at position 0 in the stack. */
    private boolean hasThis = false;

    private int currentPointerIndex = 0;

    public Stack<MethodActionPointer> getActionPointers( ) {
        return actionPointers;
    }

    /**
     * A convenience method, equivalent to {@link #getActionPointers()}.pop()
     * 
     * @return the popped top action pointer in the stack.
     */
    public MethodActionPointer popActionPointer( ) {
        return actionPointers.pop();
    }

    /**
     * A convenience method, equivalent to {@link #getActionPointers()}.push(pointer)
     * 
     * @param pointer the pointer to be pushed to the top of the pointer stack.
     */
    public void pushActionPointer(MethodActionPointer pointer) {
        actionPointers.push(pointer);
    }

    /**
     * A convenience method, equivalent to
     * 
     * <pre>
     * {@link #getActionPointers()}.push(new {@link MethodActionPointer}({@link #getCurrentPointerIndex()}))
     * </pre>
     */
    public void pushActionPointer( ) {
        actionPointers.push(new MethodActionPointer(currentPointerIndex));
    }

    public MethodActionPointer getVariable(int index) {
        if (hasThis && index == 0) {
            return new MethodActionThisPointer();
        }
        int parameterIndexOffset = (hasThis ? 1 : 0); // If we have a "this." at index 0, it increases the offset of everything else by 1
        if (index < parameters.size() + parameterIndexOffset) {
            return parameters.get(index - parameterIndexOffset);
        }
        if (variableMap.containsKey(index)) {
            return variableMap.get(index);
        }
        throw new IllegalArgumentException("There isn't a variable at index " + index + "!");
    }

    public void setVariable(int index, MethodActionPointer value) { // TODO type logic
        int parameterIndexOffset = (hasThis ? 1 : 0); // See above
        if (index < parameters.size() + parameterIndexOffset) {
            throw new IllegalArgumentException("Can't overwrite parameters with variables!");
        }
        variableMap.put(index, value);
    }

    public int getCurrentPointerIndex( ) {
        return currentPointerIndex;
    }

    public void setCurrentPointerIndex(int currentPointerIndex) {
        this.currentPointerIndex = currentPointerIndex;
    }

    public int getAndIncrementCurrentPointerIndex( ) {
        return currentPointerIndex++;
    }

    public void addParameter(ClassPath parameterType) {
        parameters.add(new MethodActionParameterPointer(parameters.size(), parameterType));
    }

    public Collection<? extends MethodActionParameterPointer> getParameters( ) {
        return Collections.unmodifiableCollection(parameters);
    }

    public void setHasThis(boolean hasThis) {
        this.hasThis = hasThis;
    }
}
