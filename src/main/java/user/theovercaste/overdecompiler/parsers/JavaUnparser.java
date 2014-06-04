package user.theovercaste.overdecompiler.parsers;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.FieldData;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedField;

public class JavaUnparser extends AbstractUnparser {
    @Override
    public ClassData unparseClass(ParsedClass c) {
        ConstantPool pool = new ConstantPool(10);
        ClassData unparsed = new ClassData(pool);
        // TODO annotations
        for (ParsedField f : c.getFields()) {
            unparsed.addField(new FieldData(new FieldFlagHandler(f.getFlags()), pool.addUtf8(f.getName()), pool.addUtf8(f.getType().toMangled())));
        }
        return null;
    }

    public static class Factory implements AbstractUnparser.Factory {
        @Override
        public AbstractUnparser createUnparser( ) {
            return new JavaUnparser();
        }
    }
}
