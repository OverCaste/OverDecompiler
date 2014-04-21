package user.theovercaste.overdecompiler.parserdata.naming;

import java.util.Set;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;

public class DefaultLocalVariableNamer extends AbstractLocalVariableNamer {
	protected static final char[] SHORTHAND_NUMBER_VARIABLES = {'i', 'j', 'k'};

	@Override
	public String getLocalVariableName(ClassPath type, Set<String> usedVariableNames, LocalVariableScope scope) {
		switch (scope) {
			case FOR_LOOP_HEADER:
				return getFromForHeader(type, usedVariableNames);
			default:
				return getFromFirstLetter(type, usedVariableNames);
		}
	}

	protected String getFromForHeader(ClassPath type, Set<String> usedVariableNames) {
		if (ClassPath.LONG.equalsType(type) || ClassPath.INTEGER.equalsType(type) || ClassPath.SHORT.equalsType(type) || ClassPath.OBJECT_BYTE.equalsType(type)
				|| ClassPath.OBJECT_LONG.equalsType(type) || ClassPath.OBJECT_INTEGER.equalsType(type) || ClassPath.OBJECT_SHORT.equalsType(type) || ClassPath.OBJECT_BYTE.equalsType(type)) {
			// for(int i = 0; i < 5; i++) for example, i is used for all numeric types.
			return getUniqueModulusName(usedVariableNames, SHORTHAND_NUMBER_VARIABLES);
		}
		else {
			return getFromFirstLetter(type, usedVariableNames);
		}
	}

	protected String getFromFirstLetter(ClassPath type, Set<String> usedVariableNames) {
		return getUniqueNumberedName(usedVariableNames, type.getClassName().substring(0, 0).toLowerCase());
	}
}
