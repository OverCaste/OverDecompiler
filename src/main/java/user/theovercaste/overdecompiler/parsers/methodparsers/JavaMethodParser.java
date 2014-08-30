package user.theovercaste.overdecompiler.parsers.methodparsers;

import java.io.*;
import java.util.*;

import user.theovercaste.overdecompiler.attributes.*;
import user.theovercaste.overdecompiler.attributes.LineNumberTableAttribute.LineNumberTableValue;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.instructions.Instructions;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

import com.google.common.base.Optional;

public class JavaMethodParser extends AbstractMethodParser {
    @Override
    public void parseMethodActions(ClassData fromClass, ParsedClass toClass, MethodData origin, ParsedMethod target) throws InvalidConstantPoolPointerException, InvalidAttributeException {
        Optional<CodeAttribute> code = AttributeTypes.getWrappedAttribute(origin.getAttributes(), fromClass.getConstantPool(), CodeAttribute.class);
        if (!code.isPresent()) {
            return;
        }
        Optional<LineNumberTableAttribute> lineNumberTable = AttributeTypes.getWrappedAttribute(origin.getAttributes(), fromClass.getConstantPool(), LineNumberTableAttribute.class);
        HashMap<Integer, Integer> lineNumberMap = new HashMap<>();
        if (lineNumberTable.isPresent()) {
            for (LineNumberTableValue value : lineNumberTable.get().getTable()) {
                lineNumberMap.put(value.getStartPc(), value.getLineNumber());
            }
        }
        try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(code.get().getCode()))) {
            ArrayDeque<Instruction> instructionList = new ArrayDeque<>();
            int instructionIndexCounter = 0;
            int instructionByteIndexCounter = 0;
            for (int opcode = din.read(); opcode >= 0; opcode = din.read()) {
                Instruction.Factory f = Instructions.getFactory(opcode);
                f.setByteIndex(instructionByteIndexCounter);
                f.setInstructionIndex(instructionIndexCounter);
                if (lineNumberMap.containsKey(instructionByteIndexCounter)) {
                    f.setLineNumber(lineNumberMap.get(instructionByteIndexCounter));
                } else {
                    f.setHasLineNumber(false);
                }
                // TODO add block code
                Instruction i = f.load(opcode, din);
                System.out.println("Instruction: " + i.getClass().getName());
                instructionList.push(i);
                instructionByteIndexCounter += i.getByteSize();
                instructionByteIndexCounter++;
                instructionIndexCounter++;
            }
            Stack<MethodMember> memberStack = new Stack<MethodMember>();
            while(!instructionList.isEmpty()) {
                Instruction i = instructionList.pop();
                i.modifyStack(memberStack);
                if(i.isAction()) {
                    memberStack.push(i.getAction(fromClass, memberStack));
                }
            }
            while(!memberStack.isEmpty()) {
                target.addAction(memberStack.pop());
            }
        } catch (IOException | InvalidInstructionException e) {
            e.printStackTrace();
        } catch (InstructionParsingException e) {
            e.printStackTrace();
        }
    }
}
