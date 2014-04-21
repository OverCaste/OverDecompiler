package user.theovercaste.overdecompiler.parserdata.naming;

import java.util.Set;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;

public abstract class AbstractLocalVariableNamer {
	public abstract String getLocalVariableName(ClassPath type, Set<String> usedVariableNames, LocalVariableScope scope);

	/**
	 * Creates a unique string of characters from the given array.<br>
	 * Examples: aa, bab, ii, ij, ik
	 * 
	 * @param usedNames The set of already used names that won't be returned.
	 * @param availableCharacters The characters to use to generate this unique name.
	 * @return
	 */
	protected static String getUniqueModulusName(Set<String> usedNames, char[] availableCharacters) {
		StringBuilder nameBuilder = new StringBuilder();
		int c = 0;
		while ((nameBuilder.length() == 0) || ((usedNames != null) && usedNames.contains(nameBuilder.toString()))) {
			nameBuilder.setLength(0); // Wipe it.
			int i = c;
			do {
				nameBuilder.append(availableCharacters[i % availableCharacters.length]);
				i /= availableCharacters.length;
			} while (i > 0);
			nameBuilder.reverse();
			c++;
		}
		return nameBuilder.toString();
	}

	protected static String getUniqueNumberedName(Set<String> usedNames, String prefix) {
		StringBuilder nameBuilder = new StringBuilder();

		int c = 0;
		while ((nameBuilder.length() == 0) || ((usedNames != null) && usedNames.contains(nameBuilder.toString()))) {
			nameBuilder.setLength(0);
			nameBuilder.append(prefix);
			nameBuilder.append(c);
			c++;
		}
		return nameBuilder.toString();
	}
}
