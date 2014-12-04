package user.theovercaste.overdecompiler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.theovercaste.overdecompiler.exceptions.ArgumentParsingException;
import user.theovercaste.overdecompiler.goals.*;

public class OverDecompiler {
    private static final Logger logger = LoggerFactory.getLogger(OverDecompiler.class);

    public static void main(String[] args) {
        try {
            ArgumentHandler handler = new ArgumentHandler(args);
            if (handler.getArgumentsSize() < 1) {
                ArgumentHandler.sendDefaultUsageMessage(logger);
                return;
            }
            AbstractGoalFactory f = handler.getClassArgument("goal", "user.theovercaste.overdecompiler.goals", GoalDecompile.Factory.getInstance(), AbstractGoalFactory.class);
            AbstractGoal a = f.createAction();
            a.execute(handler);
        } catch (ArgumentParsingException e) {
            logger.error(e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            ArgumentHandler.sendDefaultUsageMessage(logger);
        }
    }
}
