package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Stack;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryFieldReference;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.exceptions.WrongConstantPoolPointerTypeException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionGetStaticField;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public class InstructionGetStatic extends Instruction {
    private final int nameIndex;

    public InstructionGetStatic(int opcode, int byteIndex, int instructionIndex, int lineNumber, int nameIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.nameIndex = nameIndex;
    }

    public InstructionGetStatic(int opcode, int byteIndex, int instructionIndex, int nameIndex) {
        super(opcode, byteIndex, instructionIndex);
        this.nameIndex = nameIndex;
    }

    public ConstantPoolEntryFieldReference getField(ConstantPool constantPool) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry e = constantPool.get(nameIndex);
        if (e instanceof ConstantPoolEntryFieldReference) {
            return (ConstantPoolEntryFieldReference) e;
        }
        throw WrongConstantPoolPointerTypeException.constructException(nameIndex, constantPool, ConstantPoolEntryFieldReference.class);
    }

    @Override
    public boolean isAction( ) {
        return true;
    }

    @Override
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        ConstantPoolEntryFieldReference f;
        try {
            f = getField(originClass.getConstantPool());
            return new MethodActionGetStaticField(f.getName(originClass.getConstantPool()), new ClassPath(f.getClassName(originClass.getConstantPool()).replace("/", ".")));
        } catch (InvalidConstantPoolPointerException e) {
            throw new InstructionParsingException(e);
        }
    }

    @Override
    public int getByteSize( ) {
        return 2;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xb2};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionGetStatic load(int opcode, DataInputStream din) throws IOException {
            int nameIndex = din.readUnsignedShort();
            return new InstructionGetStatic(opcode, byteIndex, instructionIndex, lineNumber, nameIndex);
        }
    }
}
