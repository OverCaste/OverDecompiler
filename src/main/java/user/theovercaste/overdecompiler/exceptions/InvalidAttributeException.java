package user.theovercaste.overdecompiler.exceptions;

import user.theovercaste.overdecompiler.attributes.Attribute;

/**
 * This exception is thrown if the parsing of the binary data of an {@link Attribute} fails.
 * 
 * @author <a href="http://www.reddit.com/user/TheOverCaste/">OverCaste</a>
 *
 */
public class InvalidAttributeException extends Exception {
    private static final long serialVersionUID = -8768866280526920613L;

    public InvalidAttributeException( ) {
        super();
    }

    public InvalidAttributeException(String message) {
        super(message);
    }

    public InvalidAttributeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAttributeException(Throwable cause) {
        super(cause);
    }
}
