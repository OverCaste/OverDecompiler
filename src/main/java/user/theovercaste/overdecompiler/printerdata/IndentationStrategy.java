package user.theovercaste.overdecompiler.printerdata;

/**
 * A Strategy to determine what kind of indentation to put in the decompiled source code.<br>
 * Examples would be using tabs, or a certain number of spaces.
 * 
 * @author <a href="http://www.reddit.com/user/TheOverCaste/">OverCaste</a>
 *
 */
public interface IndentationStrategy {
    /**
     * Return a prefix string for a specific indentation level.
     * 
     * @param indentationLevel an integer which starts at 0 indicating a method level indentation, with no upper bound.
     * @return a string representing the prefix indentation level to be added before decompiled elements.
     */
    public String getIndentation(int indentationLevel);
}
