package user.theovercaste.overdecompiler.attributes;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.util.AttributeData;

/**
 * A class which loads an attribute into its constituent elements.<br>
 * Please note that this loader must return an Attribute of the exact class as it's parent attribute:
 * 
 * <pre>
 * {@code (CodeAttribute's .loader() returns a AttributeLoader<Code>)}
 * </pre>
 * 
 * @author <a href="http://www.reddit.com/user/TheOverCaste/">OverCaste</a>
 */
public interface AttributeLoader<T extends Attribute> {
    public abstract T load(AttributeData a, ConstantPool constantPool) throws InvalidAttributeException;
}
