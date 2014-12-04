package user.theovercaste.overdecompiler.rawclassdata;

import java.util.*;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.util.AttributableElement;
import user.theovercaste.overdecompiler.util.AttributeData;

import com.google.common.collect.Lists;

public class ClassData implements AttributableElement {
    private final int majorVersion;
    private final int minorVersion;
    private final ConstantPool constantPool;
    private int classId;
    private int parentId;
    private int flags;
    private final List<MethodData> methods = Lists.newArrayList();
    private final List<FieldData> fields = Lists.newArrayList();
    private final List<Integer> interfaces = Lists.newArrayList();
    private final List<ClassData> nestedClasses = Lists.newArrayList();
    private final List<AttributeData> attributes = Lists.newArrayList();

    public ClassData(int majorVersion, int minorVersion, ConstantPool constantPool) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.constantPool = constantPool;
    }

    public int getMajorVersion( ) {
        return majorVersion;
    }

    public int getMinorVersion( ) {
        return minorVersion;
    }

    public void addMethod(MethodData m) {
        methods.add(m);
    }

    public void addField(FieldData f) {
        fields.add(f);
    }

    public void addInterface(int i) {
        interfaces.add(i);
    }

    public void addNestedClass(ClassData c) {
        nestedClasses.add(c);
    }

    public void addAttribute(AttributeData d) {
        attributes.add(d);
    }

    public int getClassId( ) {
        return classId;
    }

    public int getParentId( ) {
        return parentId;
    }

    public int getFlagMask( ) {
        return flags;
    }

    public Collection<Integer> getInterfaces( ) {
        return Collections.unmodifiableCollection(interfaces);
    }

    public Collection<FieldData> getFields( ) {
        return Collections.unmodifiableCollection(fields);
    }

    public Collection<MethodData> getMethods( ) {
        return Collections.unmodifiableCollection(methods);
    }

    @Override
    public Collection<AttributeData> getAttributes( ) {
        return Collections.unmodifiableCollection(attributes);
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public ConstantPool getConstantPool( ) {
        return constantPool;
    }
}