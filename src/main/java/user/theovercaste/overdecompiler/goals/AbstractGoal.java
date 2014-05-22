package user.theovercaste.overdecompiler.goals;

import java.io.PrintStream;

import user.theovercaste.overdecompiler.ArgumentHandler;
import user.theovercaste.overdecompiler.exceptions.ArgumentParsingException;

public abstract class AbstractGoal {
    public abstract void execute(ArgumentHandler h) throws ArgumentParsingException;

    protected abstract String[] getUsage( );

    protected boolean isHelpRequest(ArgumentHandler h) {
        return h.checkFlagExists('?') || h.checkFlagExists("help");
    }

    public void sendUsageMessage(PrintStream out) {
        for (String s : getUsage()) {
            out.println(s);
        }
    }
}
