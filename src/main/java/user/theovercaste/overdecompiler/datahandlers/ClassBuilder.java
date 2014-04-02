package user.theovercaste.overdecompiler.datahandlers;

import java.util.ArrayList;
import java.util.List;

import user.theovercaste.overdecompiler.codeinternals.Class;
import user.theovercaste.overdecompiler.codeinternals.ClassFlag;
import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.ClassType;
import user.theovercaste.overdecompiler.codeinternals.Field;
import user.theovercaste.overdecompiler.codeinternals.Method;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryClass;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;

public class ClassBuilder {
	private final ConstantPoolEntry[] constantPool;

	private String name;
	private ClassType type;
	private ClassPath parent;
	private final List<ClassFlag> flags = new ArrayList<>();
	private final List<Method> methods = new ArrayList<>();
	private final List<Field> fields = new ArrayList<>();
	private final List<Class> nestedClasses = new ArrayList<>();

	public ClassBuilder(ConstantPoolEntry[] constantPool) {
		this.constantPool = constantPool;
	}

	public ClassBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public ClassBuilder setName(int nameId) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(nameId, constantPool.length);
		ConstantPoolEntry poolEntry = constantPool[nameId];
		if (poolEntry instanceof ConstantPoolEntryClass) {
			setName(((ConstantPoolEntryClass) poolEntry).getName(constantPool));
		} else {
			throw PoolPreconditions.getInvalidType(constantPool, nameId);
		}
		return this;
	}

	public ClassBuilder setType(ClassType type) {
		this.type = type;
		return this;
	}

	public ClassBuilder loadFlags(AccessFlagHandler flags) {
		if (flags.isAbstract()) {
			type = ClassType.ABSTRACT_CLASS;
		} else if (flags.isAnnotation()) {
			type = ClassType.ANNOTATION;
		} else if (flags.isEnum()) {
			type = ClassType.ENUM;
		} else if (flags.isInterface()) {
			type = ClassType.INTERFACE;
		} else {
			type = ClassType.CLASS;
		}
		this.flags.clear();
		if (flags.isPublic()) {
			this.flags.add(ClassFlag.PUBLIC);
		}
		if (flags.isFinal()) {
			this.flags.add(ClassFlag.FINAL);
		}
		return this;
	}

	public ClassBuilder addMethod(Method m) {
		methods.add(m);
		return this;
	}

	public ClassBuilder addField(Field f) {
		fields.add(f);
		return this;
	}

	public ClassBuilder addNestedClass(Class c) {
		nestedClasses.add(c);
		return this;
	}

	public ClassBuilder setParent(ClassPath parent) {
		this.parent = parent;
		return this;
	}

	public ClassBuilder setParent(int parentId) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(parentId, constantPool.length);
		ConstantPoolEntry poolEntry = constantPool[parentId];
		if (poolEntry instanceof ConstantPoolEntryClass) {
			setParent(new ClassPath(((ConstantPoolEntryClass) poolEntry).getName(constantPool)));
		} else {
			throw PoolPreconditions.getInvalidType(constantPool, parentId);
		}
		return this;
	}

	public Class build( ) {
		Class clazz = new Class(name, type, parent);
		clazz.addFlags(flags);
		clazz.addMethods(methods);
		clazz.addFields(fields);
		clazz.addNestedClasses(nestedClasses);
		return clazz;
	}
}
