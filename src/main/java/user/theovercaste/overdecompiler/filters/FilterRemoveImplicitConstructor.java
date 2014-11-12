package user.theovercaste.overdecompiler.filters;

import user.theovercaste.overdecompiler.codeinternals.ClassFlag;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parserdata.methodmembers.*;

import com.google.common.collect.Iterables;

public class FilterRemoveImplicitConstructor implements Filter {
    @Override
    public boolean apply(ParsedClass clazz) {
        if (clazz.getConstructors().size() != 1) { // It's only implicit if there's one, otherwise they all have to be defined.
            return false;
        }
        ParsedMethod soleConstructor = Iterables.getOnlyElement(clazz.getConstructors());
        if (soleConstructor.getArguments().size() != 0 || soleConstructor.getMembers().size() != 2) {
            return false; // The implicit constructor has no arguments, and it's actions are super(); return;
        }
        if (soleConstructor.getFlags().size() != 1 || Iterables.getOnlyElement(soleConstructor.getFlags()).equals(ClassFlag.PUBLIC)) {
            return false; // The implicit constructor is 'public' and nothing else.
        }
        int index = 0;
        for (MethodMember m : soleConstructor.getMembers()) {
            if (index == 0 && (m instanceof MethodActionSuperConstructor)) {
                if (((MethodActionSuperConstructor) m).getArguments().size() != 0) {
                    return false;
                }
            } else if (index == 1 && (m instanceof MethodActionReturnVoid)) {
                // Second argument always return void.
            } else {
                return false;
            }
            index++;
        }
        clazz.removeConstructor(soleConstructor);
        return true;
    }
}
