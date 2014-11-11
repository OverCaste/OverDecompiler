package user.theovercaste.overdecompiler.parsers.methodparsers;

import java.util.*;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.parserdata.method.*;
import user.theovercaste.overdecompiler.printerdata.variablenamers.VariableNamer;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class MethodPrintingContext {
    private final Multiset<Integer> referenceCounts = HashMultiset.create();
    private final List<MethodMember> members;
    private final VariableNamer varNamer;
    private final Set<Integer> inlinedReferences = new HashSet<Integer>();
    
    public MethodPrintingContext(List<MethodMember> members, VariableNamer varNamer) {
        Preconditions.checkNotNull(members, "members can't be null!");
        Preconditions.checkNotNull(varNamer, "variable namer can't be null!");
        this.members = members;
        this.varNamer = varNamer;
    }

    public String getVariableName(int index, boolean isParameter, ClassPath type) {
        return varNamer.getVariableName(index, isParameter, type);
    }

    /**
     * Increments the reference counter for the specified variable.<br>
     * This is so that variables that are referenced only once can be inlined.
     * 
     * @param reference the reference to which a count should be increased for.
     */
    public void addReference(MethodActionPointer reference) {
        if(!reference.getClass().equals(MethodActionPointer.class)) { //TODO make better, this is pretty ugly.
            return;
        }
        referenceCounts.add(reference.index);
    }

    public int getReferences(Integer pointer) {
        return referenceCounts.count(pointer);
    }

    MethodMember getMember(int index) {
        return members.get(index);
    }

    public void setReferenceInlined(int index) {
        inlinedReferences.add(index);
    }
    
    public boolean isReferenceInlined(int index) {
        return inlinedReferences.contains(index);
    }

    public boolean isActionInlined(MethodAction action) {
        if(action instanceof MethodActionSetVariable) {
            if(inlinedReferences.contains(((MethodActionSetVariable)action).getVariableIndex())) { //The only actions that are 'inlined' are setters of single references.
                return true;
            }
        }
        return false;
    }
}
