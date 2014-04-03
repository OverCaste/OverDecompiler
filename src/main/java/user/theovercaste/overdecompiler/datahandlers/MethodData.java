package user.theovercaste.overdecompiler.datahandlers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.attributes.Attribute;
import user.theovercaste.overdecompiler.attributes.Attributes;
import user.theovercaste.overdecompiler.attributes.CodeAttribute;
import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.Method;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryUtf8;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.InvalidInstructionException;
import user.theovercaste.overdecompiler.exceptions.PoolPreconditions;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.instructions.InstructionFactory;

public class MethodData {
	private final MethodFlagHandler flags;
	private final int nameIndex;
	private final int descriptorIndex;
	private final Attribute[] attributes;
	private final Stack<Instruction> stack;

	public MethodData(MethodFlagHandler flags, int nameIndex, int descriptorIndex, Attribute[] attributes) {
		this.flags = flags;
		this.nameIndex = nameIndex;
		this.descriptorIndex = descriptorIndex;
		this.attributes = attributes;
		stack = new Stack<Instruction>();
	}

	public MethodFlagHandler getFlagHandler( ) {
		return flags;
	}

	public int getNameIndex( ) {
		return nameIndex;
	}

	public int getDescriptorIndex( ) {
		return descriptorIndex;
	}

	public Attribute[] getAttributes( ) {
		return attributes;
	}

	public String getName(ConstantPoolEntry[] constantPool) {
		return constantPool[nameIndex].toString();
	}

	public String getDescription(ConstantPoolEntry[] constantPool) {
		return constantPool[descriptorIndex].toString();
	}

	private void processStack(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		Attribute a = null;
		for (Attribute ai : attributes) {
			try {
				if (ai.getName(constantPool).equals("Code")) {
					a = ai;
					break;
				}
			} catch (InvalidConstantPoolPointerException e) {
				e.printStackTrace();
			}
		}
		if (a == null) {
			return;
		}
		CodeAttribute code = Attributes.wrapAttribute(a, constantPool, CodeAttribute.class);
		for (Attribute ai : code.getAttributes()) {
			try {
				System.out.println("Code attribute: " + ai.getName(constantPool));
			} catch (InvalidConstantPoolPointerException e) {
				e.printStackTrace();
			}
		}
		try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(code.getCode()))) {
			Stack<Instruction> stack = new Stack<Instruction>();
			while (din.available() > 0) {
				Instruction instruction = InstructionFactory.loadInstruction(din.readUnsignedByte(), din);
				stack.push(instruction);
				System.out.println("Instruction: " + instruction.getClass().getName());
			}
			this.stack.clear();
			this.stack.addAll(stack);
		} catch (IOException | InvalidInstructionException e) {
			e.printStackTrace();
		}
	}

	public ClassPath getReturnType(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		PoolPreconditions.assertPoolRange(descriptorIndex, constantPool.length);
		ConstantPoolEntry e = constantPool[descriptorIndex];
		if (e instanceof ConstantPoolEntryUtf8) {
			String descriptor = e.toString();
			int closingParenIndex = descriptor.indexOf(')');
			return ClassPath.getMangledPath(descriptor.substring(closingParenIndex + 1, descriptor.length()));
		}
		throw PoolPreconditions.getInvalidType(constantPool, descriptorIndex);
	}

	/**
	 * Converts this byte wrapped data into a machine readable method.
	 * 
	 * @param constantPool The constant pool defined for this method's constants.
	 * @return The converted method.
	 * @throws InvalidConstantPoolPointerException if the constant pool is invalid for parsing.
	 */
	public Method toMethod(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
		processStack(constantPool);
		Method ret = new Method(null, getName(constantPool), flags);
		return ret;
	}

	public static MethodData loadMethodInfo(DataInputStream din) throws IOException {
		MethodFlagHandler flagHandler = new MethodFlagHandler(din.readUnsignedShort());
		int nameIndex = din.readUnsignedShort();
		int descriptorIndex = din.readUnsignedShort();
		Attribute[] attributes = new Attribute[din.readUnsignedShort()];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i] = Attributes.loadAttribute(din);
		}
		return new MethodData(flagHandler, nameIndex, descriptorIndex, attributes);
	}
}
