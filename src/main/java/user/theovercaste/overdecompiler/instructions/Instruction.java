package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodDecompileContext;

public abstract class Instruction {
    /**
     * The byte ID of this instruction.
     */
    protected final int opcode;
    /**
     * The byte location in the instructions stack of this specific instruction. Used for goto and other statements.
     */
    protected final int byteIndex;
    /**
     * The index in the instruction stack of this specific instruction.
     */
    protected final int instructionIndex;

    protected final int lineNumber;

    protected final boolean hasLineNumber;

    protected Instruction(int opcode, int byteIndex, int instructionIndex, int lineNumber) {
        this.opcode = opcode;
        this.byteIndex = byteIndex;
        this.instructionIndex = instructionIndex;
        this.lineNumber = lineNumber;
        hasLineNumber = lineNumber >= 0;
    }

    protected Instruction(int opcode, int byteIndex, int instructionIndex) {
        this.opcode = opcode;
        this.byteIndex = byteIndex;
        this.instructionIndex = instructionIndex;
        lineNumber = -1;
        hasLineNumber = false;
    }

    public abstract boolean isAction( );

    public abstract MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException;

    public abstract void modifyStack(MethodDecompileContext ctx);

    public int getOpcode( ) {
        return opcode;
    }

    public int getByteIndex( ) {
        return byteIndex;
    }

    public abstract int getByteSize( );

    public static abstract class Factory {
        protected int byteIndex;
        protected int instructionIndex;
        protected int lineNumber;
        protected boolean hasLineNumber;

        public void setByteIndex(int byteIndex) {
            this.byteIndex = byteIndex;
        }

        public void setInstructionIndex(int instructionIndex) {
            this.instructionIndex = instructionIndex;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
            hasLineNumber = lineNumber >= 0;
        }

        public void setHasLineNumber(boolean hasLineNumber) {
            this.hasLineNumber = hasLineNumber;
        }

        public abstract Instruction load(int opcode, DataInputStream din) throws IOException;
    }
}
