package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.EndOfStackException;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parserdata.methodmembers.MethodActionGetArrayLength;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodDecompileContext;

public class InstructionArrayLength extends AbstractInstructionDirectAction {
    public InstructionArrayLength(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
    }

    public InstructionArrayLength(int opcode, int byteIndex, int instructionIndex) {
        super(opcode, byteIndex, instructionIndex);
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        if (ctx.getActionPointers().isEmpty()) {
            throw new EndOfStackException("There was no array to get the length of!");
        }
        System.out.println("Action pointers: " + ctx.getActionPointers());
        MethodActionPointer value = ctx.popActionPointer();
        return new MethodActionGetArrayLength(value);
    }

    @Override
    public int getByteSize( ) {
        return 0;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xBE};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionArrayLength load(int opcode, DataInputStream din) throws IOException {
            return new InstructionArrayLength(opcode, byteIndex, instructionIndex, lineNumber);
        }
    }

}
