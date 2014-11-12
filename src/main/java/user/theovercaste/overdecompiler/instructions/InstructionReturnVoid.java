package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodActionReturnVoid;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodDecompileContext;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.return">return</a>
 */
public class InstructionReturnVoid extends AbstractInstructionDirectAction {
    public InstructionReturnVoid(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionReturnVoid(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        return new MethodActionReturnVoid();
    }

    @Override
    public int getByteSize( ) {
        return 0;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xb1};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionReturnVoid load(int opcode, DataInputStream din) throws IOException {
            return new InstructionReturnVoid(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }
}
