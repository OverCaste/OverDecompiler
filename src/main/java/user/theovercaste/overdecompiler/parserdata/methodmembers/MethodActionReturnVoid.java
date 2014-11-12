package user.theovercaste.overdecompiler.parserdata.methodmembers;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodPrintingContext;

public class MethodActionReturnVoid extends MethodAction {
    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return "return";
    }

    @Override
    public ClassPath getClassType( ) {
        return null;
    }
}
