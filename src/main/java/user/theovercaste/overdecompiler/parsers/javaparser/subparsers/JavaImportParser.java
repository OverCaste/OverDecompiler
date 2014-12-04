package user.theovercaste.overdecompiler.parsers.javaparser.subparsers;

import java.util.HashSet;
import java.util.Set;

import user.theovercaste.overdecompiler.parseddata.*;
import user.theovercaste.overdecompiler.parseddata.annotation.ParsedAnnotation;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodMember;
import user.theovercaste.overdecompiler.util.*;

public class JavaImportParser {
    public void parseImports(ParsedClass clazz) { // TODO annotations
        Set<String> imports = new HashSet<String>();
        if (clazz.getParent().isPresent()) {
            attemptAddImport(imports, clazz.getParent().get());
        }
        for (ParsedField f : clazz.getFields()) {
            attemptAddImport(imports, f);
        }
        for (ParsedMethod m : clazz.getMethods()) {
            for (ClassPath arg : m.getArguments()) {
                attemptAddImport(imports, arg);
            }
            for (ClassPath e : m.getExceptions()) {
                attemptAddImport(imports, e);
            }
            for (MethodMember member : m.getMembers()) {
                attemptAddImport(imports, member);
            }
            for (ParsedAnnotation a : clazz.getAnnotations()) {
                attemptAddImport(imports, a);
            }
            attemptAddImport(imports, m.getReturnType());
        }
        for (String i : imports) {
            clazz.addImport(i);
        }
    }

    private static void attemptAddImport(Set<String> imports, String imp) {
        if (imp.contains(".") && !imp.endsWith(".") && !imp.startsWith("java.lang.")) { // TODO don't import from the same package as this class, either
            imports.add(imp);
        }
    }

    private static void attemptAddImport(Set<String> imports, ClassPath c) {
        attemptAddImport(imports, c.asArray(0).getFullDeclaration());
    }

    private static void attemptAddImport(Set<String> imports, Importable i) {
        Iterable<ClassPath> elementImports = i.getImportedElements();
        if (elementImports == null) {
            return; // TODO warning
        }
        for (ClassPath p : elementImports) {
            attemptAddImport(imports, p);
        }
    }
}
