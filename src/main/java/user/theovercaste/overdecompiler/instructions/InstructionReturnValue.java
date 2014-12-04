package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.EndOfStackException;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodActionReturnValue;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;

public class InstructionReturnValue extends AbstractInstructionDirectAction {
    public InstructionReturnValue(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionReturnValue(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        if (ctx.getActionPointers().isEmpty()) {
            throw new EndOfStackException("There was no value to be returned!");
        }
        MethodActionPointer value = ctx.popActionPointer();
        return new MethodActionReturnValue(value);
    }

    @Override
    public int getByteSize( ) {
        return 0;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xAC, 0xAD, 0xAE, 0xAF, 0xB0}; // ireturn, lreturn, freturn, dreturn, areturn
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionReturnValue load(int opcode, DataInputStream din) throws IOException {
            return new InstructionReturnValue(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }
}
