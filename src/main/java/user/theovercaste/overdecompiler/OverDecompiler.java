package user.theovercaste.overdecompiler;

import user.theovercaste.overdecompiler.exceptions.ArgumentParsingException;
import user.theovercaste.overdecompiler.goals.AbstractGoal;
import user.theovercaste.overdecompiler.goals.AbstractGoalFactory;
import user.theovercaste.overdecompiler.goals.GoalDecompile;

public class OverDecompiler {
    public static void main(String[] args) {
        try {
            ArgumentHandler handler = new ArgumentHandler(args);
            if (handler.getArgumentsSize() < 1) {
                ArgumentHandler.sendDefaultUsageMessage();
                return;
            }
            AbstractGoalFactory f = handler.getClassArgument("goal", "user.theovercaste.overdecompiler.goals", GoalDecompile.Factory.getInstance(), AbstractGoalFactory.class);
            AbstractGoal a = f.createAction();
            a.execute(handler);
        } catch (ArgumentParsingException e) {
            System.out.println(e.getMessage());
            ArgumentHandler.sendDefaultUsageMessage();
        }
    }
}
