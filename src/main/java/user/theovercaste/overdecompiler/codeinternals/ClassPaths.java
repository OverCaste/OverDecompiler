package user.theovercaste.overdecompiler.codeinternals;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

/**
 * A class that contains utility methods for {@link ClassPath}s.
 */
public final class ClassPaths {
    private ClassPaths( ) {
        //Non-instantiable.
    }
    
    /**
     * Transforms an iterable of ClassPath into an Iterable of Strings of their definitions.
     * 
     * @see {@link ClassPath#getDefinition()}
     * 
     * @param source The source collection to get ClassPaths from.
     * @return A string iterable backed by the source.
     */
    public static Iterable<String> transformDefinitions(Iterable<ClassPath> source) {
        return Iterables.transform(source, new Function<ClassPath, String>() { //Would be a lambda...
            @Override
            public String apply(ClassPath input) {
                return input.getDefinition();
            }
        });
    }
}
