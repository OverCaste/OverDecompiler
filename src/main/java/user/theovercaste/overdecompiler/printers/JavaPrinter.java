package user.theovercaste.overdecompiler.printers;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import user.theovercaste.overdecompiler.codeinternals.*;
import user.theovercaste.overdecompiler.parserdata.*;
import user.theovercaste.overdecompiler.parserdata.method.*;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodPrintingContext;
import user.theovercaste.overdecompiler.printerdata.variablenamers.VariableNamer;

import com.google.common.base.*;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public abstract class JavaPrinter extends AbstractPrinter {
    private final VariableNamer varNamer;
    
    public JavaPrinter(VariableNamer varNamer) {
        this.varNamer = varNamer;
    }
    
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
        Optional<ClassPath> parent = clazz.getParent();
        out.print(" ");
        if (parent.isPresent()) {
            out.print("extends ");
            out.print(parent.get().getClassName());
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

    protected boolean printConstructors(ParsedClass clazz, PrintStream out) {
        int count = 0;
        for (ParsedMethod c : clazz.getConstructors()) {
            if (printConstructor(clazz, c, out)) {
                count++;
            }
        }
        return count > 0;
    }

    protected boolean printMethods(ParsedClass clazz, PrintStream out) {
        int count = 0;
        for (ParsedMethod m : clazz.getMethods()) {
            if (printMethodHeader(clazz, m, out)) {
                printMethodCode(clazz, m, out);
                printFooter(clazz, out);
                count++;
            }
        }
        return count > 0;
    }

    protected void printMethodCode(ParsedClass clazz, ParsedMethod m, PrintStream out) {
        MethodPrintingContext ctx = new MethodPrintingContext(new ArrayList<>(m.getMembers()), varNamer);
        for(MethodMember member : m.getMembers()) {
            member.countReferences(ctx);
        }
        for(MethodMember member : m.getMembers()) {
            if(member instanceof MethodActionSetVariable) {
                int index = ((MethodActionSetVariable) member).getVariableIndex();
                if(ctx.getReferences(index) == 1) {
                    ctx.setReferenceInlined(index);
                }
            }
        }
        for (MethodMember member : m.getMembers()) {
            printMethodMember(clazz, m, member, out, ctx);
        }
    }

    protected void printMethodAction(ParsedClass clazz, ParsedMethod m, MethodAction action, PrintStream out, MethodPrintingContext ctx) {
        if(action.isForceInlined() || ctx.isActionInlined(action)) {
            return;
        }
        out.print(action.getStringValue(clazz, m, ctx));
        out.println(";");
    }

    protected void printMethodBlock(ParsedClass clazz, ParsedMethod m, MethodBlock block, PrintStream out, MethodPrintingContext ctx) {
        out.print(block.getBlockHeader(clazz, m, ctx));
        out.println(" {");
        for (MethodMember subMember : block.getMembers()) {
            printMethodMember(clazz, m, subMember, out, ctx);
        }
        out.println("}");
    }

    protected void printMethodMember(ParsedClass clazz, ParsedMethod m, MethodMember member, PrintStream out, MethodPrintingContext ctx) {
        if (member.getType() == MethodMember.Type.ACTION) {
            printMethodAction(clazz, m, (MethodAction) member, out, ctx);
        }
        else if (member.getType() == MethodMember.Type.BLOCK) {
            printMethodBlock(clazz, m, (MethodBlock) member, out, ctx);
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
            if (m.getReturnType().equals(ClassPath.VOID)) {
                out.print("void ");
            } else {
                ClassPath returnType = m.getReturnType();
                clazz.addImport(returnType);
                out.print(returnType.getDefinition());
                out.print(" ");
            }
            out.print(m.getName());
        }
        out.print("(");
        final AtomicInteger counter = new AtomicInteger();
        if (!m.getArguments().isEmpty()) {
            out.print(Joiner.on(", ").join(Iterables.transform(m.getArguments(), new Function<ClassPath, String>() {
                @Override
                public String apply(ClassPath input) {
                    return input.getDefinition() + " " + varNamer.getVariableName(counter.getAndIncrement(), true, input);
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
