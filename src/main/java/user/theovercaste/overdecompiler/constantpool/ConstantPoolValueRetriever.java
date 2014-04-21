package user.theovercaste.overdecompiler.constantpool;

import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;

public class ConstantPoolValueRetriever {
	private static ConstantPoolValueRetriever instance;

	private ConstantPoolValueRetriever( ) {
		// Do nothing
	}

	public String getNameAndTypeName(ConstantPoolEntry[] constantPool, int index) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(index, constantPool.length);
		ConstantPoolEntry entry = constantPool[index];
		if (entry instanceof ConstantPoolEntryNameAndType) {
			return ((ConstantPoolEntryNameAndType) entry).getName(constantPool);
		}
		throw PoolPreconditions.getInvalidType(constantPool, index);
	}

	public String getNameAndTypeDescription(ConstantPoolEntry[] constantPool, int index) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(index, constantPool.length);
		ConstantPoolEntry entry = constantPool[index];
		if (entry instanceof ConstantPoolEntryNameAndType) {
			return ((ConstantPoolEntryNameAndType) entry).getDescription(constantPool);
		}
		throw PoolPreconditions.getInvalidType(constantPool, index);
	}

	public String getString(ConstantPoolEntry[] constantPool, int index) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(index, constantPool.length);
		ConstantPoolEntry entry = constantPool[index];
		if (entry instanceof ConstantPoolEntryUtf8) {
			return ((ConstantPoolEntryUtf8) entry).getValue();
		}
		if (entry instanceof ConstantPoolEntryString) {
			return getString(constantPool, ((ConstantPoolEntryString) entry).getStringIndex());
		}
		throw PoolPreconditions.getInvalidType(constantPool, index);
	}

	public String getString(ConstantPoolEntry entry) throws InvalidConstantPoolPointerException {
		if (entry instanceof ConstantPoolEntryUtf8) {
			return ((ConstantPoolEntryUtf8) entry).getValue();
		}
		throw PoolPreconditions.getInvalidType(entry);
	}

	public String getClassName(ConstantPoolEntry[] constantPool, int index) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(index, constantPool.length);
		ConstantPoolEntry entry = constantPool[index];
		if (entry instanceof ConstantPoolEntryClass) {
			return ((ConstantPoolEntryClass) entry).getName(constantPool);
		}
		throw PoolPreconditions.getInvalidType(constantPool, index);
	}

	public static ConstantPoolValueRetriever getInstance( ) {
		if (instance == null) {
			instance = new ConstantPoolValueRetriever();
		}
		return instance;
	}
}
