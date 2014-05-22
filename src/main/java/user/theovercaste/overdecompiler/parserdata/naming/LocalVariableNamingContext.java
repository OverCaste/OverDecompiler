package user.theovercaste.overdecompiler.parserdata.naming;

import java.util.HashSet;
import java.util.Set;

public class LocalVariableNamingContext {
    protected final Set<String> usedVariableNames = new HashSet<String>();

    protected void addUsedName(String name) {
        usedVariableNames.add(name);
    }
}
