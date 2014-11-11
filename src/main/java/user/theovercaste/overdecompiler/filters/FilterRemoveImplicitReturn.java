package user.theovercaste.overdecompiler.filters;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parserdata.method.*;

import com.google.common.collect.Iterables;

/**
 * This filter removes the implicit 'return' at the end of method definitions.
 * 
 * @author <a href="http://www.reddit.com/user/TheOverCaste/">OverCaste</a>
 */
public class FilterRemoveImplicitReturn implements Filter {
    @Override
    public boolean apply(ParsedClass clazz) {
        boolean success = false;
        for (ParsedMethod m : Iterables.concat(clazz.getMethods(), clazz.getConstructors())) {
            if (m.getMembers().isEmpty()) {
                continue;
            }
            MethodMember lastMember = Iterables.getLast(m.getMembers());
            if (lastMember instanceof MethodAction) {
                MethodAction lastAction = (MethodAction) lastMember;
                if (lastAction instanceof MethodActionReturnVoid) {
                    m.removeMember(lastMember);
                    success = true;
                }
            }

        }
        return success;
    }
}
