package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Stack;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.constantpool.*;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.parserdata.methodmembers.*;

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
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        try {
            ConstantPool constantPool = originClass.getConstantPool();
            ConstantPoolPreconditions.checkEntryType(constantPool, methodIndex, REQUIRED_TYPES);
            String descriptor = constantPool.getReferenceType(methodIndex);
            Collection<ClassPath> arguments = ClassPath.getMethodArguments(descriptor);
            MethodAction[] actions = new MethodAction[arguments.size()];
            for (int i = 0; i < arguments.size(); i++) {
                if (stack.isEmpty()) {
                    throw new EndOfStackException();
                }
                MethodMember a = stack.pop();
                if (a instanceof MethodAction) {
                    actions[i] = (MethodAction) a;
                } else {
                    throw new InstructionParsingException("Parameter " + i + "'s type isn't printable! (" + a.getClass().getName() + ")");
                }
            }
            return new MethodActionInvokeMethodStatic(ClassPath.getInternalPath(constantPool.getReferenceClassName(methodIndex)), constantPool.getReferenceName(methodIndex),
                    ImmutableList.copyOf(actions));
        } catch (InvalidConstantPoolPointerException e) {
            throw new InstructionParsingException(e);
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
