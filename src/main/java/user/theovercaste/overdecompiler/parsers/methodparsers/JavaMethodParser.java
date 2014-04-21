package user.theovercaste.overdecompiler.parsers.methodparsers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.attributes.CodeAttribute;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.InvalidInstructionException;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.instructions.InstructionFactory;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;

public class JavaMethodParser extends AbstractMethodParser {
	@Override
	public void parseMethodActions(ClassData fromClass, ParsedClass toClass, MethodData origin, ParsedMethod value) throws InvalidConstantPoolPointerException {
		CodeAttribute code = getCodeAttribute(fromClass, origin.getAttributes());
		if (code == null) {
			return;
		}
		try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(code.getCode()))) {
			Stack<MethodAction> stack = new Stack<MethodAction>();
			for (int opcode = din.read(); opcode >= 0; opcode = din.read()) {
				Instruction i = InstructionFactory.loadInstruction(opcode, din);
				if (i.isAction()) {
					try {
						MethodAction a = i.getAction(fromClass, stack);
						stack.push(a);
					} catch (InstructionParsingException e) {
						e.printStackTrace();
					}
				}
			}
			for (MethodAction a : stack) {
				value.addAction(a);
			}

		} catch (IOException | InvalidInstructionException e) {
			e.printStackTrace();
		}
	}
}
