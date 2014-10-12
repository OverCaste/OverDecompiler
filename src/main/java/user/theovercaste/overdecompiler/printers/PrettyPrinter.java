package user.theovercaste.overdecompiler.printers;

import java.io.*;

import user.theovercaste.overdecompiler.parserdata.*;
import user.theovercaste.overdecompiler.parserdata.method.*;

import com.google.common.base.Strings;

public class PrettyPrinter extends EnumCompatiblePrinter {
    public static final String INDENT_CHAR = "  "; // two spaces
    protected static final String INDENT_LONG = Strings.repeat(INDENT_CHAR, 100); // A huge block of indented chars

    private int currentIndent = 0;

    @Override
    public void print(ParsedClass c, OutputStream out) throws IOException {
        print(c, out, 0);
    }

    protected void print(ParsedClass c, OutputStream out, int indent) throws IOException {
        currentIndent = indent;
        PrintStream printer = (out instanceof PrintStream ? (PrintStream) out : new PrintStream(out));
        if (printPackage(c, printer)) {
            printer.println();
        }
        if (printImports(c, printer)) {
            printer.println();
        }
        printClassHeader(c, printer);

        if (printFields(c, printer)) {
            printer.println();
        }
        if(printConstructors(c, printer)) {
            printer.println();
        }
        printMethods(c, printer);
        printFooter(c, printer); // Close the class
    }

    @Override
    protected boolean printField(ParsedClass clazz, ParsedField f, PrintStream out) {
        // if (!f.getFlags().contains(FieldFlag.SYNTHETIC)) { //This can be abused by nefarious people setting the synthetic flag to hide elements.
        out.print(getIndent(currentIndent));
        return super.printField(clazz, f, out);
        // }
        // return false;
    }

    @Override
    protected boolean printEnumField(ParsedClass clazz, ParsedField f, boolean last, PrintStream out) {
        out.print(getIndent(currentIndent));
        return super.printEnumField(clazz, f, last, out);
    }

    @Override
    protected boolean printEnumFields(ParsedClass clazz, PrintStream out) {
        if (super.printEnumFields(clazz, out)) {
            out.println();
            return true;
        }
        return false;
    }

    @Override
    protected void printMethodAction(ParsedClass clazz, ParsedMethod m, MethodAction action, PrintStream out) {
        out.print(getIndent(currentIndent));
        super.printMethodAction(clazz, m, action, out);
    }
    
    @Override
    protected void printMethodBlock(ParsedClass clazz, ParsedMethod m, MethodBlock block, PrintStream out) {
        out.print(getIndent(currentIndent));
        out.print(block.getBlockHeader(clazz, m));
        out.println(" {");
        currentIndent++;
        for(MethodMember subMember : block.getMembers()) {
            printMethodMember(clazz, m, subMember, out);
        }
        currentIndent--;
        out.print(getIndent(currentIndent));
        out.println("}");
    }
    
    @Override
    protected boolean printMethodHeader(ParsedClass clazz, ParsedMethod m, PrintStream out) {
        // if (!m.getFlags().contains(MethodFlag.SYNTHETIC)) { //This can be abused by nefarious people setting the synthetic flag to hide elements.
        out.print(getIndent(currentIndent++));
        return super.printMethodHeader(clazz, m, out);
        // }
        // return false;
    }

    @Override
    protected void printClassHeader(ParsedClass clazz, PrintStream out) {
        out.print(getIndent(currentIndent++));
        super.printClassHeader(clazz, out);
    }

    @Override
    protected void printFooter(ParsedClass clazz, PrintStream out) {
        out.print(getIndent(--currentIndent));
        out.println("}");
    }

    protected String getIndent(int level) {
        if (level <= 0) {
            return "";
        }
        return INDENT_LONG.substring(0, level * INDENT_CHAR.length());
    }

    public static class Factory implements AbstractPrinterFactory {
        private static final Factory instance = new Factory();

        private Factory( ) {
            // do nothing
        }

        @Override
        public AbstractPrinter createPrinter( ) {
            return new PrettyPrinter();
        }

        public static Factory getInstance( ) {
            return instance;
        }
    }
}
