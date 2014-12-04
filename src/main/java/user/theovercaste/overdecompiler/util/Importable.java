package user.theovercaste.overdecompiler.util;

/**
 * An interface which symbolizes a member which contains a concatenated class path which must be imported.
 * 
 * @author <a href="http://www.reddit.com/user/TheOverCaste/">OverCaste</a>
 */
public interface Importable {
    public Iterable<ClassPath> getImportedElements( );
}
