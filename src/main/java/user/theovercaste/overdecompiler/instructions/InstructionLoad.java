package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;

public class InstructionLoad extends AbstractInstructionStackModifier {
    private final int referenceIndex;

    public InstructionLoad(int opcode, int byteIndex, int instructionIndex, int lineNumber, int referenceIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.referenceIndex = referenceIndex;
    }

    public InstructionLoad(int opcode, int byteIndex, int instructionIndex, int referenceIndex) {
        super(opcode, byteIndex, instructionIndex);
        this.referenceIndex = referenceIndex;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x15, 0x16, 0x17, 0x18, 0x19};
    }

    public int getNumber( ) {
        return referenceIndex;
    }

    @Override
    public void modifyStack(MethodDecompileContext ctx) {
        MethodActionPointer var = ctx.getVariable(getNumber());
        ctx.getActionPointers().push(var);
    }

    @Override
    public int getByteSize( ) {
        return 1;
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionLoad load(int opcode, DataInputStream din) throws IOException {
            int referenceIndex = din.readUnsignedByte();
            return new InstructionLoad(opcode, byteIndex, instructionIndex, lineNumber, referenceIndex);
        }
    }
}
