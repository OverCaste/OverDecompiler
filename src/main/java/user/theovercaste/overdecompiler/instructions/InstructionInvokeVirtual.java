package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.EmptyStackException;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.constantpool.*;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionInvokeMethod;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodDecompileContext;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class InstructionInvokeVirtual extends AbstractInstructionDirectAction {
    private static final ImmutableSet<Class<? extends ConstantPoolEntry>> REQUIRED_TYPES =
            ImmutableSet.<Class<? extends ConstantPoolEntry>> builder().add(ConstantPoolEntryMethodReference.class).build(); // .of doesn't like these for some reason...

    private final int methodIndex;

    public InstructionInvokeVirtual(int opcode, int byteIndex, int instructionIndex, int lineNumber, int methodIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.methodIndex = methodIndex;
    }

    public InstructionInvokeVirtual(int opcode, int byteIndex, int instructionIndex, int methodIndex) {
        super(opcode, byteIndex, instructionIndex);
        this.methodIndex = methodIndex;
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        try {
            ConstantPool constantPool = originClass.getConstantPool();
            ConstantPoolPreconditions.checkEntryType(constantPool, methodIndex, REQUIRED_TYPES);
            String descriptor = constantPool.getReferenceType(methodIndex);
            Collection<ClassPath> argumentTypes = ClassPath.getMethodArguments(descriptor);
            ClassPath returnType = ClassPath.getMethodReturnType(descriptor);
            MethodActionPointer[] arguments = new MethodActionPointer[argumentTypes.size()];
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = ctx.popActionPointer();
            }
            MethodActionPointer invokee = ctx.popActionPointer();
            return new MethodActionInvokeMethod(invokee, constantPool.getReferenceName(methodIndex), ImmutableList.copyOf(arguments), returnType); // todo transfer
        } catch (InvalidConstantPoolPointerException | EmptyStackException e) {
            throw new InstructionParsingException(e);
        }
    }

    @Override
    public int getByteSize( ) {
        return 2;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xb6};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionInvokeVirtual load(int opcode, DataInputStream din) throws IOException {
            int methodIndex = din.readUnsignedShort();
            return new InstructionInvokeVirtual(opcode, byteIndex, instructionIndex, lineNumber, methodIndex);
        }
    }
}
