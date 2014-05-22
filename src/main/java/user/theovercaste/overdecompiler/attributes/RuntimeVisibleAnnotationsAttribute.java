package user.theovercaste.overdecompiler.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import user.theovercaste.overdecompiler.constantpool.ConstantPoolEntry;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class RuntimeVisibleAnnotationsAttribute extends ParsedAttribute {
    private final Annotation[] annotations;

    public RuntimeVisibleAnnotationsAttribute(Annotation[] annotations) {
        this.annotations = annotations;
    }

    public ImmutableList<Annotation> getAnnotations( ) {
        return ImmutableList.copyOf(annotations);
    }

    public static String getName( ) {
        return "RuntimeVisibleAnnotations";
    }

    public static Parser parser( ) {
        return new Parser();
    }

    public static class Annotation {
        protected final int typeIndex;
        protected final ImmutableMap<Integer, ElementValue> values;

        public Annotation(int typeIndex, ImmutableMap<Integer, ElementValue> values) {
            this.typeIndex = typeIndex;
            this.values = values;
        }

        public static Annotation load(DataInputStream din) throws IOException {
            int typeIndex = din.readUnsignedShort();
            int valuesSize = din.readUnsignedShort();
            ImmutableMap.Builder<Integer, ElementValue> values = ImmutableMap.<Integer, ElementValue> builder();
            for (int i = 0; i < valuesSize; i++) {
                int elementNameIndex = din.readUnsignedShort();
                ElementValue v = ElementValue.loadElement(din);
                values.put(elementNameIndex, v);
            }
            return new Annotation(typeIndex, values.build());
        }
    }

    public static class ElementValue {

        public static ElementValue loadElement(DataInputStream din) {
            throw new UnsupportedOperationException(); // TODO
        }
    }

    public static class Parser extends ParsedAttribute.Parser<RuntimeVisibleAnnotationsAttribute> {
        @Override
        public RuntimeVisibleAnnotationsAttribute parse(AttributeData a, ConstantPoolEntry[] constantPool) throws InvalidAttributeException {
            try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.data))) {
                Annotation[] annotations = new Annotation[din.readUnsignedShort()];
                for (int i = 0; i < annotations.length; i++) {
                    annotations[i] = Annotation.load(din);
                }
                return new RuntimeVisibleAnnotationsAttribute(annotations);
            } catch (IOException e) {
                throw new InvalidAttributeException(e);
            }
        }
    }
}
