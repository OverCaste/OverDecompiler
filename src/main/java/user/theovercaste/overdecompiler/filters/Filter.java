package user.theovercaste.overdecompiler.filters;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;

public interface Filter {
    public boolean apply(ParsedClass clazz);
}
