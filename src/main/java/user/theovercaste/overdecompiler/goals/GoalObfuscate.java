package user.theovercaste.overdecompiler.goals;

import java.io.File;
import java.util.Collection;

import user.theovercaste.overdecompiler.ArgumentHandler;
import user.theovercaste.overdecompiler.exceptions.ArgumentParsingException;

import com.google.common.collect.ObjectArrays;

public class GoalObfuscate extends AbstractGoalByteEditor {
	@Override
	public void execute(ArgumentHandler h) throws ArgumentParsingException {
		if (isHelpRequest(h)) {
			sendUsageMessage(System.out);
			return;
		}
		processData(h.getArgument(h.getArgumentsSize() - 1), false);
	}

	@Override
	protected void processFiles(Collection<File> f) {

	}

	@Override
	protected String[] getUsage( ) {
		return ObjectArrays.concat(ArgumentHandler.getDefaultUsage(), new String[] {
				"  --parser (class)\t\tSpecify a parser to be used.",
				"  --printer (class)\t\tSpecify the printer to be used.",
				"  --threaded  -t\tSpecify that decompilation should be done concurrently.",
		}, String.class);
	}

	public static class Factory implements AbstractGoalFactory {
		private static final Factory instance = new Factory();

		private Factory( ) {
			// Do nothing
		}

		public static Factory getInstance( ) {
			return instance;
		}

		@Override
		public AbstractGoal createAction( ) {
			return new GoalObfuscate();
		}
	}
}
