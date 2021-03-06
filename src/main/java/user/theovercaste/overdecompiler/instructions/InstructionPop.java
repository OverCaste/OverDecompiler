package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;

public class InstructionPop extends AbstractInstructionStackModifier {
    public InstructionPop(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionPop(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public void modifyStack(MethodDecompileContext ctx) {
        ctx.popActionPointer();
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x57};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionPop load(int opcode, DataInputStream din) throws IOException {
            return new InstructionPop(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }
}
