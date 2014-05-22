package user.theovercaste.overdecompiler.attributes;

import java.util.Collection;

public interface AttributableElement {
    public void addAttribute(AttributeData d);

    public Collection<AttributeData> getAttributes( );
}
