package user.theovercaste.overdecompiler.instructions;

import java.io.IOException;

import user.theovercaste.overdecompiler.exceptions.InvalidInstructionException;
import user.theovercaste.overdecompiler.instructions.comparisons.InstructionIfNotEqual;

import com.google.common.collect.ImmutableMap;

public final class Instructions {
    private Instructions( ) {
        // No instantiation
    }

    private static final ImmutableMap<Integer, Instruction.Factory> factoryMap = getFactoryMap();

    private static ImmutableMap<Integer, Instruction.Factory> getFactoryMap( ) {
        ImmutableMap.Builder<Integer, Instruction.Factory> builder = ImmutableMap.<Integer, Instruction.Factory> builder();
        registerInstruction(builder, InstructionConstantNull.getOpcodes(), InstructionConstantNull.factory());
        registerInstruction(builder, InstructionConstantNumber.getOpcodes(), InstructionConstantNumber.factory());
        registerInstruction(builder, InstructionByteIntegerPush.getOpcodes(), InstructionByteIntegerPush.factory());
        registerInstruction(builder, InstructionNew.getOpcodes(), InstructionNew.factory());
        registerInstruction(builder, InstructionArrayLength.getOpcodes(), InstructionArrayLength.factory());

        // Dummy instructions with no data and no MethodAction, used by other instructions
        registerInstruction(builder, InstructionDup.getOpcodes(), InstructionDup.factory());
        registerInstruction(builder, InstructionPop.getOpcodes(), InstructionPop.factory());

        // Comparison instructions, used in goto -> block conversion
        registerInstruction(builder, InstructionIfNotEqual.getOpcodes(), InstructionIfNotEqual.factory());

        registerInstruction(builder, InstructionGetStatic.getOpcodes(), InstructionGetStatic.factory()); // This may be easier with reflection, but it would make the code brittle.
        registerInstruction(builder, InstructionInvokeVirtual.getOpcodes(), InstructionInvokeVirtual.factory());
        registerInstruction(builder, InstructionInvokeSpecial.getOpcodes(), InstructionInvokeSpecial.factory());
        registerInstruction(builder, InstructionInvokeStatic.getOpcodes(), InstructionInvokeStatic.factory());
        registerInstruction(builder, InstructionLoadConstant.getOpcodes(), InstructionLoadConstant.factory());
        registerInstruction(builder, InstructionLoad.getOpcodes(), InstructionLoad.factory());
        registerInstruction(builder, InstructionLoadNumbered.getOpcodes(), InstructionLoadNumbered.factory());
        registerInstruction(builder, InstructionStore.getOpcodes(), InstructionStore.factory());
        registerInstruction(builder, InstructionStoreNumbered.getOpcodes(), InstructionStoreNumbered.factory());
        registerInstruction(builder, InstructionAdd.getOpcodes(), InstructionAdd.factory());
        registerInstruction(builder, InstructionReturnVoid.getOpcodes(), InstructionReturnVoid.factory());
        registerInstruction(builder, InstructionReturnValue.getOpcodes(), InstructionReturnValue.factory());
        return builder.build();
    }

    private static void registerInstruction(ImmutableMap.Builder<Integer, Instruction.Factory> builder, int[] opcodes, Instruction.Factory b) {
        for (int i : opcodes) {
            builder.put(i, b); // Duplicate keys already fail
        }
    }

    public static Instruction.Factory getFactory(int id) throws IOException {
        if (factoryMap.containsKey(id)) {
            return factoryMap.get(id);
        }
        throw new InvalidInstructionException("Instruction " + id + " (" + Integer.toHexString(id) + ") isn't defined.");
    }

    public static Iterable<Instruction.Factory> getFactories( ) {
        return factoryMap.values();
    }
}
