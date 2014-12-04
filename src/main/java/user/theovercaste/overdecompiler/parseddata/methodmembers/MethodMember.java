package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.Importable;

public abstract class MethodMember implements Importable {
    protected final Type type;

    public MethodMember(Type type) {
        this.type = type;
    }

    public Type getType( ) {
        return type;
    }

    /**
     * Count the references for all of the MethodActionPointers used by this member. This is so that variables only referenced once can be inlined.
     * 
     * @param decompileContext
     */
    public abstract void countReferences(MethodPrintingContext printingContext);

    public static enum Type {
        /**
         * Members such as if statements, loops, and other blocks that contain code.
         */
        BLOCK,
        /**
         * Actual method calls, variable setters, or concrete code.
         */
        ACTION,
        /**
         * Unprintable, usually metadata and other informational data.
         */
        UNPRINTABLE;
    }
}
