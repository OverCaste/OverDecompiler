package user.theovercaste.overdecompiler.util;

import java.util.*;
import java.util.Map.Entry;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;

public class ImportList extends AbstractSet<String> {
    /** Imports that don't include an asterisk, such as 'java.io.InputStream' */
    private final Multimap<String, String> regularImports = HashMultimap.create();
    /** Imports which include a wildcard (asterisk), such as 'java.io.*' */
    private final Set<String> wildcardImports = new HashSet<String>();

    @Override
    public boolean add(String key) {
        Preconditions.checkNotNull(key, "you can't add a null key to this collection!");
        int lastPeriodIndex = key.lastIndexOf('.');
        Preconditions.checkArgument(lastPeriodIndex >= 0, "you can only add keys which contain at least one period."); // There is no such thing as 'import List' because the default package is imported automatically.
        Preconditions.checkArgument(lastPeriodIndex != key.length() - 1, "you can't add keys which end in periods to this collection."); // There is also no such thing as 'import java.util.List.'
        String className = key.substring(lastPeriodIndex + 1); // 'java.util.Set' -> 'Set'
        String packageName = key.substring(0, lastPeriodIndex);
        if (className.equals("*")) {
            return wildcardImports.add(packageName);
        } else {
            return regularImports.put(packageName, className);
        }
    }

    @Override
    public boolean contains(Object objectKey) {
        Preconditions.checkNotNull(objectKey, "this collection can't contain null keys.");
        Preconditions.checkArgument(objectKey instanceof String, "invalid type for key: " + objectKey + " (" + objectKey.getClass().getName() + ")");
        String key = (String) objectKey;
        int lastPeriodIndex = key.lastIndexOf('.');
        if (lastPeriodIndex < 0 || lastPeriodIndex == key.length() - 1) {
            return false;
        }
        String className = key.substring(lastPeriodIndex + 1);
        String packageName = key.substring(0, lastPeriodIndex);
        if (wildcardImports.contains(packageName)) {
            return true;
        }
        if (regularImports.containsEntry(packageName, className)) {
            return true;
        }
        return false;
    }

    @Override
    public Iterator<String> iterator( ) {
        return Iterators.concat(Iterables.transform(wildcardImports, new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input + ".*";
            }
        }).iterator(), Iterables.transform(regularImports.entries(), new Function<Map.Entry<String, String>, String>() {
            @Override
            public String apply(Entry<String, String> input) {
                return input.getKey() + "." + input.getValue();
            }
        }).iterator());
    }

    @Override
    public int size( ) {
        return regularImports.size() + wildcardImports.size();
    }

    @Override
    public boolean remove(Object objectKey) {
        Preconditions.checkNotNull(objectKey, "this collection can't have a null key removed.");
        Preconditions.checkArgument(objectKey instanceof String, "invalid type for key: " + objectKey + " (" + objectKey.getClass().getName() + ")");
        String key = (String) objectKey;
        int lastPeriodIndex = key.lastIndexOf('.');
        if (lastPeriodIndex < 0 || lastPeriodIndex == key.length() - 1) {
            return false;
        }
        String className = key.substring(lastPeriodIndex + 1); // 'java.util.Set' -> 'Set'
        String packageName = key.substring(0, lastPeriodIndex);
        if (className.equals("*")) {
            return wildcardImports.remove(packageName);
        } else {
            return regularImports.remove(packageName, className);
        }
    }

    @Override
    public void clear( ) {
        regularImports.clear();
        wildcardImports.clear();
    }
}
