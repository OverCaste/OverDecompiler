package user.theovercaste.overdecompiler.parseddata.annotation;

import java.util.Arrays;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public enum AnnotationMemberType {
    BYTE('B'),
    CHAR('C'),
    DOUBLE('D'),
    FLOAT('F'),
    INT('I'),
    LONG('J'),
    SHORT('S'),
    BOOLEAN('Z'),
    STRING('s'),
    ENUM('e'),
    CLASS('c'),
    ANNOTATION('@'),
    ARRAY('[');

    private final char tag;

    AnnotationMemberType(char tag) {
        this.tag = tag;
    }

    private static final ImmutableMap<Character, AnnotationMemberType> tagMap = Maps.uniqueIndex(Arrays.asList(values()), new Function<AnnotationMemberType, Character>() {
        @Override
        public Character apply(AnnotationMemberType input) {
            return input.tag;
        }
    });

    public static AnnotationMemberType getFromTag(char tag) {
        return tagMap.get(tag);
    }
}
