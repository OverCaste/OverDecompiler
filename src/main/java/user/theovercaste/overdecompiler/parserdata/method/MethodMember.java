package user.theovercaste.overdecompiler.parserdata.method;

import java.io.PrintStream;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

public abstract class MethodMember {
    protected final Type type;

    public MethodMember(Type type) {
        this.type = type;
    }

    public Type getType( ) {
        return type;
    }

    public abstract void print(ParsedClass c, ParsedMethod parent, PrintStream out);

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
