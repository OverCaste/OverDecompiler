package user.theovercaste.overdecompiler.printers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import user.theovercaste.overdecompiler.codeinternals.FieldFlag;
import user.theovercaste.overdecompiler.codeinternals.MethodFlag;
import user.theovercaste.overdecompiler.parsers.ParsedClass;
import user.theovercaste.overdecompiler.parsers.ParsedField;
import user.theovercaste.overdecompiler.parsers.ParsedMethod;

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
		printMethods(c, printer);
		printFooter(c, printer); // Close the class
	}

	@Override
	protected boolean printField(ParsedClass clazz, ParsedField f, PrintStream out) {
		if (!f.getFlags().contains(FieldFlag.SYNTHETIC)) {
			out.print(getIndent(currentIndent));
			return super.printField(clazz, f, out);
		}
		return false;
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
	protected boolean printMethods(ParsedClass clazz, PrintStream out) {
		int count = 0;
		boolean first = true;
		for (ParsedMethod m : clazz.getMethods()) {
			if (!m.getFlags().contains(MethodFlag.SYNTHETIC)) {
				if (!first) {
					out.println();
				}
				if (printMethodHeader(clazz, m, out)) {
					printFooter(clazz, out);
					first = false;
					count++;
				}
			}
		}
		return count > 0;
	}

	@Override
	protected boolean printMethodHeader(ParsedClass clazz, ParsedMethod m, PrintStream out) {
		if (!m.getFlags().contains(MethodFlag.SYNTHETIC)) {
			out.print(getIndent(currentIndent++));
			return super.printMethodHeader(clazz, m, out);
		}
		return false;
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
}
