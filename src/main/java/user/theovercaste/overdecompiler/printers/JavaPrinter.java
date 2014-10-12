package user.theovercaste.overdecompiler.printers;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import user.theovercaste.overdecompiler.codeinternals.*;
import user.theovercaste.overdecompiler.parserdata.*;
import user.theovercaste.overdecompiler.parserdata.method.*;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public abstract class JavaPrinter extends AbstractPrinter {
    protected boolean printPackage(ParsedClass clazz, PrintStream out) {
        String p = clazz.getPackage();
        if (p.length() > 0) {
            out.print("package ");
            out.print(p);
            out.println(";");
            return true;
        }
        return false;
    }

    protected boolean printImport(ParsedClass clazz, ClassPath i, PrintStream out) {
        if (i.isObject()) {
            String path = i.getSimplePath();
            if (!path.startsWith("java.lang.") && !i.equals(clazz.getClassPath())) {
                out.println("import " + path + ";");
                return true;
            }
        }
        return false;
    }

    protected boolean printImports(ParsedClass clazz, PrintStream out) {
        int count = 0;
        for (ClassPath i : clazz.getImports()) { // import is a keyword, c is already used, i it is!
            if (printImport(clazz, i, out)) {
                count++;
            }
        }
        return count > 0;
    }

    protected void printClassHeader(ParsedClass clazz, PrintStream out) {
        if (clazz.getFlags().contains(ClassFlag.PUBLIC)) {
            out.print("public ");
        }
        if (clazz.getFlags().contains(ClassFlag.FINAL)) {
            out.print("final ");
        }
        switch (clazz.getType()) {
            case ANNOTATION:
                out.print("@interface ");
                break;
            case INTERFACE:
                out.print("interface ");
                break;
            case ENUM:
                out.print("enum ");
                break;
            case ABSTRACT_CLASS:
                out.print("abstract ");
            case CLASS:
                out.print("class ");
                break;
        }
        out.print(clazz.getName());
        ClassPath parent = clazz.getParent();
        out.print(" ");
        if (!(parent.equals(ClassPath.OBJECT) || (isEnum(clazz) && parent.equals(ClassPath.OBJECT_ENUM)))) { // Doesn't extend object, or java.lang.Enum if it's an enum.
            out.print("extends ");
            out.print(parent.getClassName());
            out.print(" ");
        }
        List<ClassPath> effectiveParents = Lists.newArrayListWithCapacity(clazz.getInterfaces().size());
        for (ClassPath p : clazz.getInterfaces()) {
            if ((clazz.getType().equals(ClassType.ANNOTATION) && p.equals(ClassPath.OBJECT_ANNOTATION))) {
                continue; // If it's an annotation, skip the 'implements annotation'
            }
            effectiveParents.add(p);
        }
        if (effectiveParents.size() > 0) {
            out.print("implements ");
            out.print(Joiner.on(", ").join(Iterables.transform(effectiveParents, new Function<ClassPath, String>() { // Transforms an Iterable of classpaths into a CSV string
                @Override
                public String apply(ClassPath input) {
                    return input.getClassName();
                }
            })));
            out.print(" ");
        }
        out.print("{");
        out.println();
    }

    protected boolean printField(ParsedClass clazz, ParsedField f, PrintStream out) {
        // if (f.getFlags().contains(FieldFlag.SYNTHETIC)) { This can be abused by nefarious people setting the synthetic flag to hide elements.
        // return false;// don't print
        // }
        if (f.getFlags().contains(FieldFlag.PUBLIC)) {
            out.print("public ");
        } else if (f.getFlags().contains(FieldFlag.PRIVATE)) {
            out.print("private ");
        } else if (f.getFlags().contains(FieldFlag.PROTECTED)) {
            out.print("protected ");
        }
        if (f.getFlags().contains(FieldFlag.STATIC)) {
            out.print("static ");
        }
        if (f.getFlags().contains(FieldFlag.FINAL)) {
            out.print("final ");
        }
        if (f.getFlags().contains(FieldFlag.TRANSIENT)) {
            out.print("transient ");
        }
        if (f.getFlags().contains(FieldFlag.VOLATILE)) {
            out.print("volatile ");
        }
        out.print(f.getType().getDefinition());
        out.print(" ");
        out.print(f.getName());
        out.print(";");
        out.println();
        return true;
    }

    protected boolean printFields(ParsedClass clazz, PrintStream out) {
        int count = 0;
        for (ParsedField f : clazz.getFields()) {
            if (printField(clazz, f, out)) {
                count++;
            }
        }
        return count > 0;
    }

    protected boolean printConstructor(ParsedClass clazz, ParsedMethod constructor, PrintStream out) {
        if (printMethodHeader(clazz, constructor, out)) {
            printMethodCode(clazz, constructor, out);
            printFooter(clazz, out);
            return true;
        }
        return false;
    }

    protected boolean checkDefaultConstructor(ParsedMethod soleConstructor) {
        if (soleConstructor.getArguments().size() == 0 && soleConstructor.getActions().size() == 2) {
            int index = 0;
            for (MethodMember m : soleConstructor.getActions()) {
                if (index == 0 && (m instanceof MethodActionSuperConstructor)) {
                    if (((MethodActionSuperConstructor) m).getArguments().size() != 0) {
                        System.out.println("Arguments: " + ((MethodActionSuperConstructor)m).getArguments());
                        return false;
                    }
                } else if (index == 1 && (m instanceof MethodActionReturnVoid)) {
                    //Second argument always return void.
                } else {
                    return false;
                }
                index++;
            }
        }
        return true;
    }

    protected boolean printConstructors(ParsedClass clazz, PrintStream out) {
        List<ParsedMethod> constructors = new ArrayList<>();
        for (ParsedMethod m : clazz.getMethods()) {
            if (m.getName().equals("<init>")) {
                constructors.add(m);
            }
        }
        if (constructors.size() == 1) {
            if (checkDefaultConstructor(constructors.get(0))) { // Don't print the implicit single, default constructor
                return false;
            }
        }
        int count = 0;
        for (ParsedMethod c : constructors) {
            if (printConstructor(clazz, c, out)) {
                count++;
            }
        }
        return count > 0;
    }

    protected boolean printMethods(ParsedClass clazz, PrintStream out) {
        int count = 0;
        for (ParsedMethod m : clazz.getMethods()) {
            if (m.getName().equals("<init>") || m.getName().equals("<clinit>")) {
                continue; // Special cases.
            }
            if (printMethodHeader(clazz, m, out)) {
                printMethodCode(clazz, m, out);
                printFooter(clazz, out);
                count++;
            }
        }
        return count > 0;
    }

    protected void printMethodCode(ParsedClass clazz, ParsedMethod m, PrintStream out) {
        int size = m.getActions().size();
        int count = 0;
        for (MethodMember action : m.getActions()) {
            if ((count == (size - 1)) && (action instanceof MethodActionReturnVoid)) {
                // A exception to printing. If the action is a return void and it's the last action it's implicit. Don't print.
            } else {
                printMethodMember(clazz, m, action, out);
            }
            count++;
        }
    }
    
    protected void printMethodAction(ParsedClass clazz, ParsedMethod m, MethodAction action, PrintStream out) {
        out.print(action.getStringValue(clazz, m));
        out.println(";");
    }
    
    protected void printMethodBlock(ParsedClass clazz, ParsedMethod m, MethodBlock block, PrintStream out) {
        out.print(block.getBlockHeader(clazz, m));
        out.println(" {");
        for(MethodMember subMember : block.getMembers()) {
            printMethodMember(clazz, m, subMember, out);
        }
        out.println("}");
    }
    
    protected void printMethodMember(ParsedClass clazz, ParsedMethod m, MethodMember member, PrintStream out) {
        if(member.getType() == MethodMember.Type.ACTION) {
            printMethodAction(clazz, m, (MethodAction)member, out);
        }
        else if(member.getType() == MethodMember.Type.BLOCK) {
            printMethodBlock(clazz, m, (MethodBlock)member, out);
        }
    }

    protected boolean printMethodHeader(final ParsedClass clazz, final ParsedMethod m, PrintStream out) {
        // if (m.getFlags().contains(MethodFlag.SYNTHETIC)) { This can be abused by nefarious people setting the synthetic flag to hide elements.
        // return false;// don't print
        // }
        if (m.getFlags().contains(MethodFlag.PUBLIC)) {
            out.print("public ");
        } else if (m.getFlags().contains(MethodFlag.PRIVATE)) {
            out.print("private ");
        } else if (m.getFlags().contains(MethodFlag.PROTECTED)) {
            out.print("protected ");
        }
        if (m.getFlags().contains(MethodFlag.ABSTRACT)) {
            out.print("abstract ");
        }
        else if (m.getFlags().contains(MethodFlag.FINAL)) {
            out.print("final ");
        }
        if (m.getFlags().contains(MethodFlag.STATIC)) {
            out.print("static ");
        }
        if (m.getFlags().contains(MethodFlag.SYNCHRONIZED)) {
            out.print("synchronized ");
        }
        if (m.getFlags().contains(MethodFlag.NATIVE)) {
            out.print("native ");
        }
        if (m.getName().equals("<init>")) { // Constructor, no type def
            out.print(clazz.getName());
        }
        else {
            if (m.getReturnType().isPresent()) {
                ClassPath returnType = m.getReturnType().get();
                out.print(returnType.getDefinition());
                out.print(" ");
            } else {
                out.print("void ");
            }
            out.print(m.getName());
        }
        out.print("(");
        if (!m.getArguments().isEmpty()) {
            out.print(Joiner.on(", ").join(Iterables.transform(m.getArguments(), new Function<ClassPath, String>() {
                @Override
                public String apply(ClassPath input) {
                    return input.getDefinition() + " " + getArgumentName(clazz, m, input);
                }
            })));
        }
        out.print(")");
        if (!m.getExceptions().isEmpty()) {
            out.print(" throws ");
            out.print(Joiner.on(", ").join(ClassPaths.transformDefinitions(m.getExceptions())));
        }
        if (m.getFlags().contains(MethodFlag.NATIVE) || m.getFlags().contains(MethodFlag.ABSTRACT)) {
            out.print(";");
        } else {
            out.print(" {");
        }
        out.println();
        return true;
    }

    protected void printFooter(ParsedClass clazz, PrintStream out) {
        out.println("}");
    }
}
