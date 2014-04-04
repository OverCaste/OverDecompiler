package user.theovercaste.overdecompiler.datahandlers;

import java.util.ArrayList;
import java.util.List;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;

public class ClassData {
	private int classId;
	private int parentId;
	private int flags;
	private final List<MethodData> methods = new ArrayList<>();
	private final List<FieldData> fields = new ArrayList<>();
	private final List<ClassData> nestedClasses = new ArrayList<>();

	public void setName(int nameId) throws InvalidConstantPoolPointerException {
		classId = nameId;
	}

	public void addMethod(MethodData m) {
		methods.add(m);
	}

	public void addField(FieldData f) {
		fields.add(f);
	}

	public void addNestedClass(ClassData c) {
		nestedClasses.add(c);
	}

	public int getClassId( ) {
		return classId;
	}

	public int getParentId( ) {
		return parentId;
	}

	public int getFlags( ) {
		return flags;
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
}
