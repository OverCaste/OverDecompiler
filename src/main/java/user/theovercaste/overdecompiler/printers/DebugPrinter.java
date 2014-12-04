package user.theovercaste.overdecompiler.printers;

import java.io.*;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodBlock;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodMember;

public class DebugPrinter implements Printer {
    @Override
    public void print(ParsedClass c, OutputStream out) throws IOException {
        PrintStream printer = (out instanceof PrintStream ? (PrintStream) out : new PrintStream(out));
        printer.println("class [" + c.getClassPath() + "] flags[" + c.getFlags() + "] parent [" + c.getParent().orNull() + "]");
        // TODO Fields if those are needed
        for (ParsedMethod method : c.getMethods()) {
            printer.println("method [" + method.getName() + "] flags[" + method.getFlags() + "] returns [" + method.getReturnType() + "]");
            for (MethodMember m : method.getMembers()) {
                if (m instanceof MethodBlock) {
                    printer.println("methodblock [" + m.getClass().getName() + "]");
                }
                else {
                    printer.println("methodmember [" + m.toString() + "]");
                }
            }
        }
    }

    public static class Factory implements AbstractPrinterFactory {
        @Override
        public Printer createPrinter( ) {
            return new DebugPrinter();
        }
    }
}
