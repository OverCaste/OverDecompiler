package user.theovercaste.overdecompiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import user.theovercaste.overdecompiler.exceptions.ArgumentParsingException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class ArgumentHandler {
	private final ImmutableList<String> argumentList;
	private final ImmutableSet<Character> booleanValues;
	private final ImmutableMap<String, String> argumentValues;

	public ArgumentHandler(String[] args) throws ArgumentParsingException {
		ImmutableList.Builder<String> argumentListBuilder = ImmutableList.<String> builder();
		ImmutableSet.Builder<Character> booleanValuesBuilder = ImmutableSet.<Character> builder();
		ImmutableMap.Builder<String, String> argumentValuesBuilder = ImmutableMap.<String, String> builder();
		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--")) {
				if (i == (args.length - 1)) { // It's the last argument
					throw new ArgumentParsingException("The argument \"" + args[i] + "\" doesn't have a defined value!");
				}
				argumentValuesBuilder.put(args[i].substring(2), args[i + 1]);
			} else if (args[i].startsWith("-")) {
				char[] chars = args[i].toCharArray();
				for (int j = 1; j < chars.length; j++) { // Skip the first character (the -)
					booleanValuesBuilder.add(chars[j]);
				}
			} else {
				argumentListBuilder.add(args[i]);
			}
		}
		argumentList = argumentListBuilder.build();
		booleanValues = booleanValuesBuilder.build();
		argumentValues = argumentValuesBuilder.build();
	}

	public static void sendUsageMessage( ) {
		System.out.println("Usage: overdecompiler (options) [file]");
		System.out.println("where possible options include:");
		System.out.println("  -help  --help  -?\t\tPrint this usage message");
		System.out.println("  --parser (class)\t\tSpecify a parser to be used.");
		System.out.println("  --printer (class)\t\tSpecify the printer to be used.");
	}

	@SuppressWarnings("unchecked")
	// Actually checked by isAssignableFrom
	public <T> T getClassArgument(String key, String defaultPackage, T def, Class<T> type) throws ArgumentParsingException {
		if (!argumentValues.containsKey(key)) {
			return def;
		}
		String parserName = argumentValues.get(key);
		if (!parserName.contains(".")) {
			parserName = defaultPackage + "." + parserName;
		}
		try {
			Class<?> clazz = Class.forName(parserName);
			if (clazz.isAssignableFrom(type)) {
				Method getInstanceMethod = clazz.getMethod("getInstance");
				return (T) getInstanceMethod.invoke(null);
			}
		} catch (ClassNotFoundException e) { // Reflection gives you a lot of errors.
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		throw new ArgumentParsingException("The -" + key + " flag's value isn't an instance of " + type.getName() + ".");
	}

	public boolean checkFlagExists(char c) {
		return booleanValues.contains(c);
	}

	public int getArgumentsSize( ) {
		return argumentList.size();
	}

	public String getArgument(int index) {
		return argumentList.get(index);
	}
}
