package user.theovercaste.overdecompiler.exceptions;

import java.util.Arrays;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class WrongConstantPoolPointerTypeException extends InvalidConstantPoolPointerException {
    private static final long serialVersionUID = 4389466630700087754L;

    public WrongConstantPoolPointerTypeException( ) {
        super();
    }

    public WrongConstantPoolPointerTypeException(String message) {
        super(message);
    }

    public WrongConstantPoolPointerTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongConstantPoolPointerTypeException(Throwable cause) {
        super(cause);
    }

    public static WrongConstantPoolPointerTypeException constructException(int index, ConstantPool pool, Class<?>... expected) {
        return new WrongConstantPoolPointerTypeException("The constant pool entry at " + index + " (" + (pool.get(index).getClass().getName()) + ") has an invalid type. Expected: " +
                Joiner.on(", ").join(Iterables.transform(Arrays.asList(expected), new Function<Class<?>, String>() {
                    @Override
                    public String apply(Class<?> input) {
                        return input.getSimpleName();
                    }
                })));
    }
}
