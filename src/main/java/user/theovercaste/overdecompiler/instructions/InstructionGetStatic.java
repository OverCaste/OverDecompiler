package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetStaticField;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public class InstructionGetStatic extends AbstractInstructionDirectAction {
    private final int fieldIndex;

    public InstructionGetStatic(int opcode, int byteIndex, int instructionIndex, int lineNumber, int fieldIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.fieldIndex = fieldIndex;
    }

    public InstructionGetStatic(int opcode, int byteIndex, int instructionIndex, int nameIndex) {
        super(opcode, byteIndex, instructionIndex);
        fieldIndex = nameIndex;
    }

    @Override
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        try {
            ConstantPool constantPool = originClass.getConstantPool();
            return new MethodActionGetStaticField(constantPool.getFieldReferenceName(fieldIndex), ClassPath.getInternalPath(constantPool.getFieldReferenceClassName(fieldIndex).replace("/", ".")));
        } catch (InvalidConstantPoolPointerException e) {
            throw new InstructionParsingException(e);
        }
    }

    @Override
    public int getByteSize( ) {
        return 2;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xb2};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionGetStatic load(int opcode, DataInputStream din) throws IOException {
            int fieldIndex = din.readUnsignedShort();
            return new InstructionGetStatic(opcode, byteIndex, instructionIndex, lineNumber, fieldIndex);
        }
    }
}
