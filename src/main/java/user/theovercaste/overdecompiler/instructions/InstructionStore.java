package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;

public class InstructionStore extends AbstractInstructionStackModifier {
    private final int value;

    public InstructionStore(int opcode, int byteIndex, int instructionIndex, int lineNumber, int value) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.value = value;
    }

    public InstructionStore(int opcode, int byteIndex, int instructionIndex, int value) {
        super(opcode, byteIndex, instructionIndex);
        this.value = value;
    }

    public int getNumber( ) {
        return value;
    }

    @Override
    public void modifyStack(MethodDecompileContext ctx) {
        ctx.pushActionPointer(ctx.getVariable(value));
    }

    @Override
    public int getByteSize( ) {
        return 1;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x36, 0x37, 0x38, 0x39, 0x3a};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionStore load(int opcode, DataInputStream din) throws IOException {
            int value = din.readUnsignedByte();
            return new InstructionStore(opcode, byteIndex, instructionIndex, lineNumber, value);
        }
    }
}
