package user.theovercaste.overdecompiler.parserdata.method;


public abstract class MethodMember {
    protected final Type type;

    public MethodMember(Type type) {
        this.type = type;
    }

    public Type getType( ) {
        return type;
    }
    
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
