package user.theovercaste.overdecompiler.attributes;

import java.io.*;

import user.theovercaste.overdecompiler.constantpool.ConstantPool;
import user.theovercaste.overdecompiler.exceptions.InvalidAttributeException;
import user.theovercaste.overdecompiler.exceptions.InvalidConstantPoolPointerException;
import user.theovercaste.overdecompiler.util.AttributeData;

import com.google.common.collect.ImmutableList;

public class RuntimeVisibleAnnotationsAttribute implements Attribute {
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

    public static Loader loader( ) {
        return new Loader();
    }

    public static class Annotation {
        protected final String type;
        protected final ImmutableList<ElementValue> values;

        public Annotation(String type, ImmutableList<ElementValue> values) {
            this.type = type;
            this.values = values;
        }

        public String getType( ) {
            return type;
        }

        public ImmutableList<ElementValue> getValues( ) {
            return values;
        }

        public static Annotation load(ConstantPool pool, DataInputStream din) throws IOException, InvalidAttributeException {
            int typeIndex = din.readUnsignedShort();
            int valuesSize = din.readUnsignedShort();
            ImmutableList.Builder<ElementValue> values = ImmutableList.builder();
            for (int i = 0; i < valuesSize; i++) {
                int elementNameIndex = din.readUnsignedShort();
                ElementValue v = ElementValue.loadElement(pool, elementNameIndex, din);
                values.add(v);
            }
            try {
                return new Annotation(pool.getUtf8(typeIndex), values.build());
            } catch (InvalidConstantPoolPointerException e) {
                throw new InvalidAttributeException(e);
            }
        }
    }

    public static class ElementValue {
        private final String name;
        private final char tag;

        // Data union, only one of these will ever not be null
        private final String constantValue;
        private final Number numericConstantValue;
        private final ElementValue[] arrayValue;
        private final String enumValueClass;
        private final String enumValueField;
        private final String classPathValue;
        private final Annotation annotationValue;

        // TODO other types http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.16.1

        public ElementValue(String name, char tag, String constantValue, Number numericConstantValue, ElementValue[] arrayValue, String enumValueClass, String enumValueField, String classPathValue,
                Annotation annotationValue) {
            this.name = name;
            this.tag = tag;
            this.constantValue = constantValue;
            this.numericConstantValue = numericConstantValue;
            this.arrayValue = arrayValue;
            this.enumValueClass = enumValueClass;
            this.enumValueField = enumValueField;
            this.classPathValue = classPathValue;
            this.annotationValue = annotationValue;
        }

        public static ElementValue loadElement(ConstantPool pool, int elementNameIndex, DataInputStream din) throws InvalidAttributeException {
            try {
                String name = pool.getUtf8(elementNameIndex);
                char tag = (char) din.readUnsignedByte();

                String constantValue = null;
                Number numericConstantValue = null;
                ElementValue[] arrayValue = null;
                String enumValueClass = null;
                String enumValueField = null;
                String classPathValue = null;
                Annotation annotationValue = null;
                switch (tag) {
                    case 'C': // char
                    case 'Z': // boolean
                    case 'B': // byte
                    case 'S': // short
                    case 'I': // int
                        numericConstantValue = pool.getInteger(din.readUnsignedShort());
                        break;
                    case 'J': // long
                        numericConstantValue = pool.getLong(din.readUnsignedShort());
                        break;
                    case 'F': // float
                        numericConstantValue = pool.getFloat(din.readUnsignedShort());
                        break;
                    case 'D': // double
                        numericConstantValue = pool.getDouble(din.readUnsignedShort());
                        break;
                    case 's': // String
                        constantValue = pool.getUtf8(din.readUnsignedShort());
                        break;
                    case '[': { // Array
                        int arraySize = din.readUnsignedShort();
                        arrayValue = new ElementValue[arraySize];
                        for (int i = 0; i < arrayValue.length; i++) {
                            arrayValue[i] = loadElement(pool, elementNameIndex, din);
                        }
                        break;
                    }
                    case 'e': { // Enum
                        enumValueClass = pool.getUtf8(din.readUnsignedShort());
                        enumValueField = pool.getUtf8(din.readUnsignedShort());
                        break;
                    }
                    case 'c': { // Class
                        classPathValue = pool.getUtf8(din.readUnsignedShort());
                        break;
                    }
                    case '@': { // Annotation
                        annotationValue = Annotation.load(pool, din);
                        break;
                    }
                    default:
                        throw new InvalidAttributeException("Unknown tag for annotation member: " + tag);
                }
                return new ElementValue(name, tag, constantValue, numericConstantValue, arrayValue, enumValueClass, enumValueField, classPathValue, annotationValue);
            } catch (InvalidConstantPoolPointerException e) {
                throw new InvalidAttributeException(e);
            } catch (IOException e) {
                throw new InvalidAttributeException(e);
            }
        }

        public String getName( ) {
            return name;
        }

        public String getConstantValue( ) {
            return constantValue;
        }

        public Number getNumericConstantValue( ) {
            return numericConstantValue;
        }

        public ElementValue[] getArrayValue( ) {
            return arrayValue;
        }

        public String getEnumValueClass( ) {
            return enumValueClass;
        }

        public String getEnumValueField( ) {
            return enumValueField;
        }

        public String getClassPathValue( ) {
            return classPathValue;
        }

        public Annotation getAnnotationValue( ) {
            return annotationValue;
        }

        public char getTag( ) {
            return tag;
        }
    }

    public static class Loader implements AttributeLoader<RuntimeVisibleAnnotationsAttribute> {
        @Override
        public RuntimeVisibleAnnotationsAttribute load(AttributeData a, ConstantPool constantPool) throws InvalidAttributeException {
            try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(a.getData()))) {
                Annotation[] annotations = new Annotation[din.readUnsignedShort()];
                for (int i = 0; i < annotations.length; i++) {
                    annotations[i] = Annotation.load(constantPool, din);
                }
                return new RuntimeVisibleAnnotationsAttribute(annotations);
            } catch (IOException e) {
                throw new InvalidAttributeException(e);
            }
        }
    }
}
