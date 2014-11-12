package user.theovercaste.overdecompiler.parsers.javaparser.methodparsers;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodActionGetThis;

/**
 * A special subset of {@link MethodActionPointer}s that references the 'this' keyword.
 * These will always return a MethodActionGetThis when referenced from a MethodDecompileContext
 * @author <a href="http://www.reddit.com/user/TheOverCaste/">OverCaste</a>
 *
 */
public final class MethodActionThisPointer extends MethodActionPointer {
    MethodActionThisPointer() {
        super(0);
    }
    
    public ClassPath getType( ) {
        throw new UnsupportedOperationException("Can't get the type of a 'this' pointer!");
    }
    
    @Override
    public MethodAction get(MethodPrintingContext printingContext) {
        return new MethodActionGetThis();
    }
    
    @Override
    public String toString( ) {
        return "[Pointer to 'this']";
    }
    
    
    @Override
    public int hashCode( ) {
        return 0;
    }
    
    @Override
    public boolean equals(Object other) {
        if(other instanceof MethodActionThisPointer) {
            return other.getClass().equals(getClass());
        }
        return false;
    }
}
