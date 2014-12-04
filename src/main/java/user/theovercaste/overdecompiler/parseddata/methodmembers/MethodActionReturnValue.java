package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;

public class MethodActionReturnValue extends MethodAction {
    private final MethodActionPointer value;

    public MethodActionReturnValue(MethodActionPointer value) {
        this.value = value;
    }

    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return "return " + value.get(ctx).getStringValue(c, parent, ctx);
    }

    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        printingContext.addReference(value);
    }
}
