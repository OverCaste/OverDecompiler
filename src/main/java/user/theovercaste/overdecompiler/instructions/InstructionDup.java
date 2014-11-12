package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodDecompileContext;

public class InstructionDup extends AbstractInstructionStackModifier {
    public InstructionDup(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionDup(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x59};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionDup load(int opcode, DataInputStream din) throws IOException {
            return new InstructionDup(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }

    @Override
    public void modifyStack(MethodDecompileContext ctx) {
        ctx.pushActionPointer(ctx.getActionPointers().peek());
    }
}
