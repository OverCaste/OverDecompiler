package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodActionGetStaticField;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;
import user.theovercaste.overdecompiler.util.ClassPath;

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
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        try {
            ConstantPool constantPool = originClass.getConstantPool();
            return new MethodActionGetStaticField(constantPool.getFieldReferenceName(fieldIndex), ClassPath.getInternalPath(constantPool.getFieldReferenceClassName(fieldIndex)),
                    ClassPath.getMangledPath(constantPool.getFieldReferenceType(fieldIndex)));
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
