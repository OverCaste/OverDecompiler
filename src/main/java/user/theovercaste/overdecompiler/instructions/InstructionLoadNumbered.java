package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.parsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodDecompileContext;

public class InstructionLoadNumbered extends AbstractInstructionStackModifier {
    public InstructionLoadNumbered(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionLoadNumbered(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    public int getNumber( ) {
        return (opcode - 0x1a) & 3; // For clarity, this is (roughly) equivalent to (getOpcodes( )[0]) % 4, which takes advantage of the fact that all of the xload_n operations are sequential.
    }

    @Override
    public void modifyStack(MethodDecompileContext ctx) {
        MethodActionPointer var = ctx.getVariable(getNumber());
        ctx.getActionPointers().push(var);
    }

    @Override
    public int getByteSize( ) {
        return 0;
    }

    public static int[] getOpcodes( ) {
        return new int[] {
                0x1a, 0x1b, 0x1c, 0x1d, // iload
                0x1e, 0x1f, 0x20, 0x21, // lload
                0x22, 0x23, 0x24, 0x25, // fload
                0x26, 0x27, 0x28, 0x29, // dload
                0x2a, 0x2b, 0x2c, 0x2d // aload
        };
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionLoadNumbered load(int opcode, DataInputStream din) throws IOException {
            return new InstructionLoadNumbered(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }
}
