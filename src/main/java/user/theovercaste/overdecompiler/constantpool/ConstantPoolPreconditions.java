package user.theovercaste.overdecompiler.constantpool;

import java.util.Collection;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.WrongConstantPoolPointerTypeException;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class ConstantPoolPreconditions {
    private ConstantPoolPreconditions( ) {
        // Hide constructor
    }

    public static void checkEntryType(ConstantPool pool, int index, Collection<Class<? extends ConstantPoolEntry>> requiredClasses) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry e = pool.get(index);
        if (!requiredClasses.contains(e.getClass())) {
            StringBuilder message = new StringBuilder();
            boolean multipleRequiredClasses = requiredClasses.size() != 1;
            message.append("The constant pool entry at index ")
                    .append(index)
                    .append("'s type is: ")
                    .append(e.getClass().getName())
                    .append(". ");
            if (multipleRequiredClasses) { // Multiple classes possible
                message.append("Required types: (");
                message.append(Joiner.on(", ").join(Iterables.transform(requiredClasses, new Function<Class<? extends ConstantPoolEntry>, String>() { // Transform classes into class names
                    @Override
                    public String apply(Class<? extends ConstantPoolEntry> input) {
                        return input.getName();
                    }
                })));
                message.append(".)");
            } else {
                message.append("Required type: ")
                        .append(Iterables.getOnlyElement(requiredClasses))
                        .append(".");
            }
            throw new WrongConstantPoolPointerTypeException(+index + "'s type is: " + e.getClass().getName()
                    + (requiredClasses.size() == 1 ? "Required type: " + Iterables.getOnlyElement(requiredClasses) : "Required types: (" + Joiner.on(", ").join(requiredClasses)));
        }
    }
}
