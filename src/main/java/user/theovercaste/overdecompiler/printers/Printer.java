package user.theovercaste.overdecompiler.printers;

import java.io.IOException;
import java.io.OutputStream;

import user.theovercaste.overdecompiler.parseddata.ParsedClass;

public interface Printer {
    public abstract void print(ParsedClass c, OutputStream out) throws IOException;
}
