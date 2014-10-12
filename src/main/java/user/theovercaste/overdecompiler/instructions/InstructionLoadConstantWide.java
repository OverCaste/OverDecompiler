package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc_w">ldc_w</a>
 */
public class InstructionLoadConstantWide extends InstructionLoadConstant {
    public InstructionLoadConstantWide(int opcode, int byteIndex, int instructionIndex, int lineNumber, int constantIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber, constantIndex);
    }

    public InstructionLoadConstantWide(int opcode, int byteIndex, int instructionIndex, int constantIndex) {
        super(opcode, byteIndex, instructionIndex, constantIndex);
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x13};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    @Override
    public int getByteSize( ) {
        return 2;
    }

    public static class Factory extends InstructionLoadConstant.Factory {
        @Override
        public InstructionLoadConstantWide load(int opcode, DataInputStream din) throws IOException {
            int constantIndex = din.readUnsignedShort();
            return new InstructionLoadConstantWide(opcode, byteIndex, instructionIndex, lineNumber, constantIndex);
        }
    }
}
