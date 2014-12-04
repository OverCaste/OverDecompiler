package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;

import user.theovercaste.overdecompiler.constantpool.*;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodAction;
import user.theovercaste.overdecompiler.parseddata.methodmembers.MethodActionInvokeMethodStatic;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodActionPointer;
import user.theovercaste.overdecompiler.parsers.javaparser.subparsers.methodparsers.MethodDecompileContext;
import user.theovercaste.overdecompiler.rawclassdata.ClassData;
import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class InstructionInvokeStatic extends AbstractInstructionDirectAction {
    private static final ImmutableSet<Class<? extends ConstantPoolEntry>> REQUIRED_TYPES =
            ImmutableSet.<Class<? extends ConstantPoolEntry>> builder().add(ConstantPoolEntryMethodReference.class).add(ConstantPoolEntryInterfaceMethodReference.class).build(); // .of doesn't like these for some reason...

    private final int methodIndex;

    public InstructionInvokeStatic(int opcode, int byteIndex, int instructionIndex, int lineNumber, int methodIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.methodIndex = methodIndex;
    }

    public InstructionInvokeStatic(int opcode, int byteIndex, int instructionIndex, int methodIndex) {
        super(opcode, byteIndex, instructionIndex);
        this.methodIndex = methodIndex;
    }

    @Override
    public MethodAction getAction(ClassData originClass, MethodDecompileContext ctx) throws InstructionParsingException {
        try {
            ConstantPool pool = originClass.getConstantPool();
            ConstantPoolPreconditions.checkEntryType(pool, methodIndex, REQUIRED_TYPES);
            String descriptor = pool.getReferenceType(methodIndex);
            Collection<ClassPath> arguments = ClassPath.getMethodArguments(descriptor);
            MethodActionPointer[] actions = new MethodActionPointer[arguments.size()];
            for (int i = 0; i < arguments.size(); i++) {
                actions[i] = ctx.popActionPointer();
            }
            System.out.println("Reference type: " + ClassPath.getMethodReturnType(pool.getReferenceType(methodIndex)) + ", " + pool.getReferenceType(methodIndex));
            return new MethodActionInvokeMethodStatic(ClassPath.getInternalPath(pool.getReferenceClassName(methodIndex)), ClassPath.getMethodReturnType(pool.getReferenceType(methodIndex)), pool.getReferenceName(methodIndex),
                    ImmutableList.copyOf(actions));
        } catch (InvalidConstantPoolPointerException e) {
            throw new InstructionParsingException("Couldn't parse instruction due to an InvalidConstantPoolPointerException.", e);
        }
    }

    @Override
    public int getByteSize( ) {
        return 2;
    }

    public static int[] getOpcodes( ) {
        return new int[] {0xb8};
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends Instruction.Factory {
        @Override
        public InstructionInvokeStatic load(int opcode, DataInputStream din) throws IOException {
            int methodIndex = din.readUnsignedShort();
            return new InstructionInvokeStatic(opcode, byteIndex, instructionIndex, lineNumber, methodIndex);
        }
    }
}
