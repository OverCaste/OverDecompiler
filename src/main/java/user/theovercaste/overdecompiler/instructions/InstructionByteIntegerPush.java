package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parseddata.methodmembers.*;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodActionGetConstant.ConstantType;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;

public class InstructionByteIntegerPush extends AbstractInstructionDirectAction {
    private final int byteValue;

    public InstructionByteIntegerPush(int opcode, int byteIndex, int instructionIndex, int lineNumber, int byteValue) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.byteValue = byteValue;
    }

    public InstructionByteIntegerPush(int opcode, int byteIndex, int instructionIndex, int byteValue) {
        super(opcode, byteIndex, instructionIndex);
        this.byteValue = byteValue;
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        return new MethodActionGetConstant(String.valueOf(byteValue), ConstantType.INT);
    }

    @Override
    public int getByteSize( ) {
        return 1;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x10};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionByteIntegerPush load(int opcode, DataInputStream din) throws IOException {
            int b = din.readByte();
            return new InstructionByteIntegerPush(opcode, byteIndex, instructionIndex, lineNumber, b);
        }
    }
}
