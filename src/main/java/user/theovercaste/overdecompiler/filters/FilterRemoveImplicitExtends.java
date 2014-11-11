package user.theovercaste.overdecompiler.filters;

import user.theovercaste.overdecompiler.codeinternals.ClassFlag;
import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;

public class FilterRemoveImplicitExtends implements Filter {
    @Override
    public boolean apply(ParsedClass clazz) {
        if (!clazz.getParent().isPresent()) {
            return false;
        }
        ClassPath parent = clazz.getParent().get();
        if (clazz.getFlags().contains(ClassFlag.ENUM) && parent.equals(ClassPath.OBJECT_ENUM)) {
            clazz.setParent(null);
            return true;
        }
        if (parent.equals(ClassPath.OBJECT)) {
            clazz.setParent(null);
            return true;
        }
        return false;
    }
}
