package user.theovercaste.overdecompiler.parsers.methodparsers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import user.theovercaste.overdecompiler.attributes.AttributeTypes;
import user.theovercaste.overdecompiler.attributes.CodeAttribute;
import user.theovercaste.overdecompiler.attributes.LineNumberTableAttribute;
import user.theovercaste.overdecompiler.attributes.LineNumberTableAttribute.LineNumberTableValue;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.InvalidInstructionException;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.instructions.InstructionFactory;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;

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
            ArrayList<Instruction> instructionList = new ArrayList<>();
            int instructionIndexCounter = 0;
            int instructionByteIndexCounter = 0;
            for (int opcode = din.read(); opcode >= 0; opcode = din.read()) {
                Instruction.Factory f = InstructionFactory.INSTANCE.getFactory(opcode);
                f.setByteIndex(instructionByteIndexCounter);
                f.setInstructionIndex(instructionIndexCounter);
                if (lineNumberMap.containsKey(instructionByteIndexCounter)) {
                    f.setLineNumber(lineNumberMap.get(instructionByteIndexCounter));
                } else {
                    f.setHasLineNumber(false);
                }
                // TODO
                Instruction i = f.load(opcode, din);
                instructionList.add(i);
                instructionByteIndexCounter += i.getByteSize();
                instructionByteIndexCounter++;
                instructionIndexCounter++;
            }

        } catch (IOException | InvalidInstructionException e) {
            e.printStackTrace();
        }
    }
}
