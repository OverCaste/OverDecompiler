package user.theovercaste.overdecompiler.printerdata.variablenamers;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;

import com.google.common.base.CaseFormat;

public class SimpleVariableNamer implements VariableNamer {
    @Override
    public String getVariableName(int stackIndex, boolean isParameter, ClassPath type) {
        if(isParameter) {
            return "param" + type.getClassName() + stackIndex; //paramString1
        }
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, type.getClassName()) + "Var" + stackIndex; // stringVar0
    }
}
