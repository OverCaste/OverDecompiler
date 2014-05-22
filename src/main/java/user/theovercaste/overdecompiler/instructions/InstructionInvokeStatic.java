package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Stack;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntryMethodReference;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.exceptions.EndOfStackException;
import user.theovercaste.overdecompiler.exceptions.InstructionParsingException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.parserdata.method.MethodAction;
import user.theovercaste.overdecompiler.parserdata.method.MethodActionInvokeMethodStatic;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;

public class InstructionInvokeStatic extends Instruction {
    private final int methodIndex;

    public InstructionInvokeStatic(int opcode, int byteIndex, int instructionIndex, int lineNumber, int methodIndex) {
        super(opcode, byteIndex, instructionIndex, lineNumber);
        this.methodIndex = methodIndex;
    }

    public InstructionInvokeStatic(int opcode, int byteIndex, int instructionIndex, int methodIndex) {
        super(opcode, byteIndex, instructionIndex);
        this.methodIndex = methodIndex;
    }

    public ConstantPoolEntryMethodReference getMethod(ConstantPoolEntry[] constantPool) throws InvalidConstantPoolPointerException {
        ConstantPoolEntry e = constantPool[methodIndex];
        if (e instanceof ConstantPoolEntryMethodReference) {
            return (ConstantPoolEntryMethodReference) e;
        }
        throw new InvalidConstantPoolPointerException("Invoke virtual has an invalid reference: " + methodIndex + ".");
    }

    @Override
    public boolean isAction( ) {
        return true;
    }

    @Override
    public MethodAction getAction(ClassData originClass, Stack<MethodMember> stack) throws InstructionParsingException {
        try {
            String descriptor = getMethod(originClass.getConstantPool()).getDescription(originClass.getConstantPool());
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
            ConstantPoolEntryMethodReference method = getMethod(originClass.getConstantPool());
            return new MethodActionInvokeMethodStatic(new ClassPath(method.getClassName(originClass.getConstantPool())), method.getName(originClass.getConstantPool()), actions);
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
