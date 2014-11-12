package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodActionAdd;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodDecompileContext;

public class InstructionAdd extends AbstractInstructionDirectAction {
    public static enum Type {
        INTEGER(ClassPath.INTEGER),
        LONG(ClassPath.LONG),
        FLOAT(ClassPath.FLOAT),
        DOUBLE(ClassPath.DOUBLE);

        private final ClassPath classpath;

        Type(ClassPath classpath) {
            this.classpath = classpath;
        }
    }

    public InstructionAdd(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionAdd(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        MethodActionPointer valueOne = ctx.getActionPointers().pop();
        MethodActionPointer valueTwo = ctx.getActionPointers().pop();
        return new MethodActionAdd(valueOne, valueTwo, getType().classpath);
    }

    @Override
    public int getByteSize( ) {
        return 0;
    }

    public Type getType( ) throws InstructionParsingException {
        switch (opcode) {
            case 0x60:
                return Type.INTEGER;
            case 0x61:
                return Type.LONG;
            case 0x62:
                return Type.FLOAT;
            case 0x63:
                return Type.DOUBLE;
        }
        throw new InstructionParsingException("Attempted to get the type of an addition instruction with an invalid opcode! (" + opcode + ")");
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x60, 0x61, 0x62, 0x63}; // iadd, ladd, fadd, dadd
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionAdd load(int opcode, DataInputStream din) throws IOException {
            return new InstructionAdd(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }
}
