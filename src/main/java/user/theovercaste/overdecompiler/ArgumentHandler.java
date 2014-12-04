package user.theovercaste.overdecompiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;

import user.theovercaste.overdecompiler.exceptions.ArgumentParsingException;

import com.google.common.collect.*;

public class ArgumentHandler {
    private final ImmutableList<String> argumentList;
    private final ImmutableSet<Character> booleanValues;
    private final ImmutableMap<String, String> argumentValues;
    private final ImmutableSet<String> argumentFlags;

    public ArgumentHandler(String[] args) throws ArgumentParsingException {
        ImmutableList.Builder<String> argumentListBuilder = ImmutableList.<String> builder();
        ImmutableSet.Builder<Character> booleanValuesBuilder = ImmutableSet.<Character> builder();
        ImmutableMap.Builder<String, String> argumentValuesBuilder = ImmutableMap.<String, String> builder();
        ImmutableSet.Builder<String> argumentFlagsBuilder = ImmutableSet.<String> builder();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                if (i != (args.length - 1)) { // It's the last argument
                    argumentValuesBuilder.put(args[i].substring(2), args[i + 1]);
                }
                argumentFlagsBuilder.add(args[i].substring(2));
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
        argumentFlags = argumentFlagsBuilder.build();
    }

    public static void sendDefaultUsageMessage(Logger logger) {
        for (String s : getDefaultUsage()) {
            logger.info(s);
        }
    }

    public static String[] getDefaultUsage( ) {
        return new String[] {
                "Usage: overdecompiler (options) [file]",
                "where possible options include:",
                "  --help  -?\t\tPrint this usage message",
                "  --action [class]\t\tSpecify the action to be performed."
        };
    }

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
                @SuppressWarnings("unchecked")
                // This is actually checked by .isAssignableFrom
                T ret = (T) getInstanceMethod.invoke(null);
                return ret;
            } else {
                throw new ArgumentParsingException("The " + key + " flag's value isn't an instance of " + type.getName() + ".");
            }
        } catch (ClassNotFoundException e) { // Reflection gives you a lot of errors.
            throw new ArgumentParsingException("Class argument for argument " + key + " couldn't be found on the classpath.");
        } catch (IllegalAccessException e) {
            throw new ArgumentParsingException("The getInstance() method of the class argument for flag " + key + " couldn't be accessed.", e);
        } catch (IllegalArgumentException e) {
            throw new AssertionError("Invoking getInstance() threw an IllegalArgumentException. This should never happen as NoSuchMethodException would be thrown instead.");
        } catch (InvocationTargetException e) {
            throw new ArgumentParsingException("The getInstance() method of the class specified for flag " + key + " threw an exception.", e);
        } catch (NoSuchMethodException e) {
            throw new ArgumentParsingException("The class specified for flag " + key + " didn't implement a getInstance() method. Is it of the proper kind?");
        } catch (SecurityException e) {
            throw new ArgumentParsingException("A SecurityException was thrown while instantiating the class specified for flag " + key, e);
        }

    }

    public boolean checkFlagExists(char c) {
        return booleanValues.contains(c);
    }

    public boolean checkFlagExists(String s) {
        return argumentFlags.contains(s);
    }

    public int getArgumentsSize( ) {
        return argumentList.size();
    }

    public String getArgument(int index) {
        return argumentList.get(index);
    }
}
