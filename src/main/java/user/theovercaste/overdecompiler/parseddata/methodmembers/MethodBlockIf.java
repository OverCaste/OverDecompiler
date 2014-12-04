package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;

public class MethodBlockIf extends MethodBlock {
    private final MethodActionComparison condition;

    public MethodBlockIf(MethodActionComparison condition) {
        this.condition = condition;
    }

    @Override
    public String getBlockHeader(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return "if(" + condition.getStringValue(c, parent, ctx) + ")";
    }
}
