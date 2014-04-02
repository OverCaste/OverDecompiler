package user.theovercaste.overdecompiler.datahandlers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import user.theovercaste.overdecompiler.attributes.Attribute;
import user.theovercaste.overdecompiler.attributes.Attributes;
import user.theovercaste.overdecompiler.attributes.CodeAttribute;
import user.theovercaste.overdecompiler.codeinternals.Method;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.InvalidInstructionException;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.instructions.InstructionReturn;
import user.theovercaste.overdecompiler.instructions.Instructions;

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

	/**
	 * Creates a method header string based on it's defined attributes.
	 * 
	 * @param flags
	 * @return (public/protected/private) (static) (abstract) (synchronized) (final) (native) [type] [name] ([args]) {
	 */
	public String getMethodDefinitionString(ConstantPoolEntry[] constantPool, ImportList imports) {
		StringBuilder s = new StringBuilder();
		if (flags.isPublic()) {
			s.append("public ");
		} else if (flags.isProtected()) {
			s.append("protected ");
		} else if (flags.isPrivate()) {
			s.append("private ");
		}
		if (flags.isStatic()) {
			s.append("static ");
		}
		if (flags.isAbstract()) {
			s.append("abstract ");
		}
		if (flags.isSynchronized()) {
			s.append("synchronized ");
		}
		if (flags.isFinal()) {
			s.append("final ");
		}
		if (flags.isNative()) {
			s.append("native ");
		}
		try {
			processStack(constantPool);
			s.append(getReturnType(constantPool)).append(" ");
			s.append(getName(constantPool));
			s.append("(");
			boolean first = true;
			for (String param : getParams(constantPool)) {
				if (!first) {
					s.append(", ");
				}
				s.append(param);
				first = false;
			}
			s.append(") {");
		} catch (InvalidConstantPoolPointerException e) {
			e.printStackTrace();
		}
		return s.toString();
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
				Instruction instruction = Instructions.loadInstruction(din.readUnsignedByte(), din);
				stack.push(instruction);
				System.out.println("Instruction: " + instruction.getClass().getName());
			}
			this.stack.clear();
			this.stack.addAll(stack);
		} catch (IOException | InvalidInstructionException e) {
			e.printStackTrace();
		}
	}

	public String getReturnType(ConstantPoolEntry[] constantPool) {
		for (Instruction i : stack) {
			if (i instanceof InstructionReturn) {
				return "void";
			}
		}
		return null;
	}

	public String[] getParams(ConstantPoolEntry[] constantPool) {
		return new String[] {"String removeThis"};
	}

	public String[] getBody(ConstantPoolEntry[] constantPool) {
		ArrayList<String> ret = new ArrayList<String>();
		while (!stack.isEmpty()) {
			Instruction i = stack.pop();
			String value = i.toJava(imports, constantPool, stack);
			if (i.printable()) {
				ret.add(value);
			}
		}
		return ret.toArray(new String[ret.size()]);
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
		return null;
	}
}
