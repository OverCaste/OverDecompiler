package user.theovercaste.overdecompiler.parserdata.methodmembers;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodPrintingContext;

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
