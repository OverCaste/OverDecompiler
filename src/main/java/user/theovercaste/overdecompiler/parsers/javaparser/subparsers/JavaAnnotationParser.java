package user.theovercaste.overdecompiler.parsers.javaparser.subparsers;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.theovercaste.overdecompiler.attributes.RuntimeVisibleAnnotationsAttribute.Annotation;
import user.theovercaste.overdecompiler.attributes.RuntimeVisibleAnnotationsAttribute.ElementValue;
import user.theovercaste.overdecompiler.parseddata.annotation.*;
import user.theovercaste.overdecompiler.util.ClassPath;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class JavaAnnotationParser {
    private static final Logger logger = LoggerFactory.getLogger(JavaAnnotationParser.class);

    public ParsedAnnotation parseAnnotation(Annotation rawAnnotation) {
        ImmutableList.Builder<AnnotationMember> annotationMemberBuilder = ImmutableList.builder();
        for (ElementValue v : rawAnnotation.getValues()) { // TODO

            annotationMemberBuilder.add(parseAnnotationMember(v));
        }
        return new ParsedAnnotation(ClassPath.getMangledPath(rawAnnotation.getType()), annotationMemberBuilder.build());
    }

    public AnnotationMember parseAnnotationMember(ElementValue rawMember) {
        AnnotationMemberType type = AnnotationMemberType.getFromTag(rawMember.getTag());
        AnnotationMember m = new AnnotationMemberConstant(rawMember.getName(), "unknown", type); // TODO move
        switch (type) {
            case ANNOTATION:
                m = new AnnotationMemberAnnotation(rawMember.getName(), parseAnnotation(rawMember.getAnnotationValue()));
                break;
            case ARRAY:
                m = new AnnotationMemberArray(
                        rawMember.getName(),
                        ImmutableList.copyOf(Iterables.transform(Arrays.asList(rawMember.getArrayValue()), new Function<ElementValue, AnnotationMember>() {
                            @Override
                            public AnnotationMember apply(ElementValue input) {
                                return parseAnnotationMember(input);
                            }
                        })));
                break;
            case ENUM:
                m = new AnnotationMemberEnum(rawMember.getName(), ClassPath.getMangledPath(rawMember.getEnumValueClass()), rawMember.getEnumValueField());
                break;
            case CLASS:
                m = new AnnotationMemberClass(rawMember.getName(), ClassPath.getMangledPath(rawMember.getClassPathValue()));
                break;
            case BOOLEAN:
                m = new AnnotationMemberConstant(rawMember.getName(), rawMember.getNumericConstantValue().intValue() == 0 ? "false" : "true", type);
                break;
            case BYTE:
                m = new AnnotationMemberConstant(rawMember.getName(), "(byte)" + Byte.toString((byte) rawMember.getNumericConstantValue().intValue()), type);
                break;
            case CHAR:
                m = new AnnotationMemberConstant(rawMember.getName(), "'" + Character.toString((char) rawMember.getNumericConstantValue().intValue()) + "'", type);
                break;
            case DOUBLE:
                m = new AnnotationMemberConstant(rawMember.getName(), "" + rawMember.getNumericConstantValue().doubleValue(), type);
                break;
            case FLOAT:
                m = new AnnotationMemberConstant(rawMember.getName(), "" + rawMember.getNumericConstantValue().floatValue() + "f", type);
                break;
            case INT:
                m = new AnnotationMemberConstant(rawMember.getName(), "" + rawMember.getNumericConstantValue().intValue(), type);
                break;
            case LONG:
                m = new AnnotationMemberConstant(rawMember.getName(), "" + rawMember.getNumericConstantValue().longValue() + "L", type);
                break;
            case SHORT:
                m = new AnnotationMemberConstant(rawMember.getName(), "(short)" + rawMember.getNumericConstantValue().shortValue(), type);
                break;
            case STRING:
                m = new AnnotationMemberConstant(rawMember.getName(), "\"" + rawMember.getConstantValue() + "\"", type);
                break;
            default:
                logger.warn("The annotation member type wasn't recognized: {} Were additional entries added to AnnotationMemberType?", type);
                break;

        }
        return m;
    }
}
