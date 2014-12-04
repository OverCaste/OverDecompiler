package user.theovercaste.overdecompiler.printerdata.variablenamers;

import user.theovercaste.overdecompiler.util.ClassPath;

public interface VariableNamer {
    /**
     * Attempt to create a good textual interpretation for a variable at the given variable index.
     * 
     * @param variableIndex the number of this variable, with 0 being the earliest defined. 0..n are parameters.
     * @param isParameter whether or not this variable was passed to a function as a parameter.
     * @param type the type of this variable at the given location.
     * @return a valid variable name based on the given information about it.
     */
    public String getVariableName(int variableIndex, boolean isParameter, ClassPath type);
}
