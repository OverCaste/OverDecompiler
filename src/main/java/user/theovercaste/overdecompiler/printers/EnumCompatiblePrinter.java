package user.theovercaste.overdecompiler.printers;

import java.io.PrintStream;

import user.theovercaste.overdecompiler.codeinternals.FieldFlag;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedField;

public abstract class EnumCompatiblePrinter extends JavaPrinter {
    protected boolean printEnumField(ParsedClass clazz, ParsedField f, boolean last, PrintStream out) {
        out.print(f.getName());
        out.println(last ? ";" : ",");
        return true;
    }

    @Override
    protected boolean printFields(ParsedClass clazz, PrintStream out) {
        if (!isEnum(clazz)) {
            return super.printFields(clazz, out);
        }

        boolean printedEnumFields = printEnumFields(clazz, out);

        int count = 0;
        for (ParsedField f : clazz.getFields()) {
            if (!isFieldEnum(f)) {
                if (printField(clazz, f, out)) {
                    count++;
                }
            }
        }
        return (count > 0) || printedEnumFields;
    }

    protected boolean printEnumFields(ParsedClass clazz, PrintStream out) {
        int enumFieldCount = 0;
        for (ParsedField f : clazz.getFields()) {
            if (isFieldEnum(f)) {
                enumFieldCount += 1;
            }
        }

        int currentEnumField = 0;
        for (ParsedField f : clazz.getFields()) {
            if (isFieldEnum(f)) {
                printEnumField(clazz, f, currentEnumField == (enumFieldCount - 1), out);
                currentEnumField++;
            }
        }
        return enumFieldCount > 0;
    }

    protected boolean isFieldEnum(ParsedField f) {
        return f.getFlags().contains(FieldFlag.ENUM);
    }
}
