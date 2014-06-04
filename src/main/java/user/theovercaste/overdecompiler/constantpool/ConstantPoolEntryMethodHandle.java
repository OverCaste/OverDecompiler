package user.theovercaste.overdecompiler.constantpool;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;

public class ConstantPoolEntryMethodHandle extends ConstantPoolEntry {
    public enum ReferenceType {
        GET_FIELD(1),
        GET_STATIC(2),
        PUT_FIELD(3),
        PUT_STATIC(4),
        INVOKE_VIRTUAL(5),
        INVOKE_STATIC(6),
        INVOKE_SPECIAL(7),
        NEW_INVOKE_SPECIAL(8),
        INVOKE_INTERFACE(9);

        public final int id;

        ReferenceType(int id) {
            this.id = id;
        }

        private static ImmutableBiMap<Integer, ReferenceType> map = ImmutableBiMap.<Integer, ReferenceType> builder() // Patented way to create a bimap of EnumValue <-> Integer
                .putAll(Maps.uniqueIndex(
                        Arrays.asList(ReferenceType.values()),
                        new Function<ReferenceType, Integer>() {
                            @Override
                            public Integer apply(ReferenceType input) {
                                return input.id;
                            }
                        }))
                .build();

        public static ReferenceType byId(int id) {
            return map.get(id);
        }
    }

    protected final int referenceKind;
    protected final int referenceIndex;

    public ConstantPoolEntryMethodHandle(int referenceKind, int referenceIndex) {
        super(ConstantPoolEntries.METHOD_HANDLE_TAG);
        this.referenceKind = referenceKind;
        this.referenceIndex = referenceIndex;
    }

    public int getReferenceKind( ) {
        return referenceKind;
    }

    public int getReferenceIndex( ) {
        return referenceIndex;
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(tag, referenceKind, referenceIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        return (((ConstantPoolEntryMethodHandle) other).referenceKind == referenceKind) && (((ConstantPoolEntryMethodHandle) other).referenceIndex == referenceIndex);
    }

    public static Factory factory( ) {
        return new Factory();
    }

    public static class Factory extends ConstantPoolEntry.Factory {
        protected int referenceKind;
        protected int referenceIndex;

        @Override
        public void read(DataInputStream din) throws IOException {
            referenceKind = din.readUnsignedByte();
            referenceIndex = din.readUnsignedShort();
        }

        @Override
        public ConstantPoolEntry build( ) {
            return new ConstantPoolEntryMethodHandle(referenceKind, referenceIndex);
        }
    }
}
