package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;

import user.theovercaste.overdecompiler.constantpool.*;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodActionSuperConstructor;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;
import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class InstructionInvokeSpecial extends AbstractInstructionDirectAction {
    private static final ImmutableSet<Class<? extends ConstantPoolEntry>> REQUIRED_TYPES =
            ImmutableSet.<Class<? extends ConstantPoolEntry>> builder().add(ConstantPoolEntryMethodReference.class).build(); // .of doesn't like these for some reason...

    private final int methodIndex;

    public InstructionInvokeSpecial(int opcode, int byteIndex, int instructionIndex, int lineNumber, int methodIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.methodIndex = methodIndex;
    }

    public InstructionInvokeSpecial(int opcode, int byteIndex, int instructionIndex, int methodIndex) {
        super(opcode, byteIndex, instructionIndex);
        this.methodIndex = methodIndex;
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        try {
            ConstantPool constantPool = originClass.getConstantPool();
            ConstantPoolPreconditions.checkEntryType(constantPool, methodIndex, REQUIRED_TYPES);
            String descriptor = constantPool.getReferenceType(methodIndex);
            Collection<ClassPath> argumentDescriptions = ClassPath.getMethodArguments(descriptor);
            MethodActionPointer[] arguments = new MethodActionPointer[argumentDescriptions.size()];
            for (int i = 0; i < arguments.length; i++) {
                MethodActionPointer a = ctx.popActionPointer();
                arguments[i] = a;
            }
            // MethodActionPointer a = ctx.popActionPointer();
            // if (a.get() instanceof MethodActionLoadVariable) {
            // if (((MethodActionLoadVariable) a.get()).getVariableIndex() == 0) {
            return new MethodActionSuperConstructor(ImmutableList.copyOf(arguments));
            // }
            // }
            // ClassPath returnType = ClassPath.getMethodReturnType(descriptor);
            // return new MethodActionInvokeMethod(a, constantPool.getReferenceName(methodIndex), ImmutableList.copyOf(arguments), returnType); // todo transfer
        } catch (InvalidConstantPoolPointerException e) {
            throw new InstructionParsingException(e);
        }
    }

    @Override
    public int getByteSize( ) {
        return 2;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xb7};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionInvokeSpecial load(int opcode, DataInputStream din) throws IOException {
            int methodIndex = din.readUnsignedShort();
            return new InstructionInvokeSpecial(opcode, byteIndex, instructionIndex, lineNumber, methodIndex);
        }
    }
}
