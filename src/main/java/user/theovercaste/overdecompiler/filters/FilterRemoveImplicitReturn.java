package user.theovercaste.overdecompiler.filters;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parseddata.methodmembers.*;

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
