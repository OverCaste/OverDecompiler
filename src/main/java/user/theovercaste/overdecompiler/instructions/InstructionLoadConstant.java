package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.constantpool.*;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.method.*;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetConstant.ConstantType;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodDecompileContext;

/**
 * Equivalent to <a href="http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc">ldc</a>
 */
public class InstructionLoadConstant extends AbstractInstructionDirectAction {
    protected final int constantIndex;

    public InstructionLoadConstant(int opcode, int byteIndex, int instructionIndex, int lineNumber, int constantIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.constantIndex = constantIndex;
    }

    public InstructionLoadConstant(int opcode, int byteIndex, int instructionIndex, int nameIndex) {
        super(opcode, byteIndex, instructionIndex);
        constantIndex = nameIndex;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0x12};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        ConstantPoolEntry e;
        try {
            e = originClass.getConstantPool().get(constantIndex);
            if (e instanceof ConstantPoolEntryInteger) {
                return new MethodActionGetConstant(String.valueOf(((ConstantPoolEntryInteger) e).getValue()), ConstantType.INT);
            }
            else if (e instanceof ConstantPoolEntryFloat) {
                return new MethodActionGetConstant(String.valueOf(((ConstantPoolEntryFloat) e).getValue()), ConstantType.FLOAT);
            }
            else if (e instanceof ConstantPoolEntryString) {
                return new MethodActionGetConstant(originClass.getConstantPool().getStringReference(constantIndex), ConstantType.STRING);
            }
            else if (e instanceof ConstantPoolEntryClass) {
                ClassPath path = ClassPath.getMangledPath(originClass.getConstantPool().getClassName(constantIndex));
                return new MethodActionGetConstant(path.getSimplePath(), ConstantType.CLASS);
            }
        } catch (InvalidConstantPoolPointerException ex) {
            throw new InstructionParsingException(ex);
        }
        throw new InstructionParsingException("Invalid type for LDC: " + (e == null ? "null" : e.getClass().getName()));
    }

    @Override
    public int getByteSize( ) {
        return 1;
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionLoadConstant load(int opcode, DataInputStream din) throws IOException {
            int constantIndex = din.readUnsignedByte();
            return new InstructionLoadConstant(opcode, byteIndex, instructionIndex, lineNumber, constantIndex);
        }
    }
}
