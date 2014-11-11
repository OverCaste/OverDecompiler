package user.theovercaste.overdecompiler.filters;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;

public interface Filter {
    public boolean apply(ParsedClass clazz);
}
