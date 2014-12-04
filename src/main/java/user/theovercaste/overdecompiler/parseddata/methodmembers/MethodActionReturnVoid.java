package user.theovercaste.overdecompiler.parseddata.methodmembers;

import java.util.Collections;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;
import user.theovercaste.overdecompiler.parseddata.ParsedMethod;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.util.ClassPath;

public class MethodActionReturnVoid extends MethodAction {
    @Override
    public String getStringValue(ParsedClass c, ParsedMethod parent, MethodPrintingContext ctx) {
        return "return";
    }

    @Override
    public ClassPath getClassType( ) {
        return null;
    }

    @Override
    public Iterable<ClassPath> getImportedElements( ) {
        return Collections.emptyList();
    }

    @Override
    public void countReferences(MethodPrintingContext printingContext) {
        // No references.
    }
}
