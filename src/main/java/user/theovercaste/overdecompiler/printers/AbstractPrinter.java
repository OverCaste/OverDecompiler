package user.theovercaste.overdecompiler.printers;

import java.io.IOException;
import java.io.OutputStream;

import user.theovercaste.overdecompiler.parserdata.ParsedClass;

public abstract class AbstractPrinter {
    public abstract void print(ParsedClass c, OutputStream out) throws IOException;
}
