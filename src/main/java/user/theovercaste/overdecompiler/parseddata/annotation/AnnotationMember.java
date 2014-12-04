package user.theovercaste.overdecompiler.parseddata.annotation;

import user.theovercaste.overdecompiler.util.Importable;

public abstract class AnnotationMember implements Importable {
    /**
     * Convert this AnnotationMember back to the sourcecode equivalent which created it.<br>
     * For example, AnnotationMemberAnnotation would look something like this:
     * 
     * <pre>
     * {@code
     * String value = "@" + annotationClassPath.getClassName()
     * }
     * </pre>
     * 
     * @return a string equivalent of the initialization of this annotation.
     */
    public abstract String getValue( );

    public abstract String getName( );

    /**
     * @return the {@link AnnotationMemberType} of this attribute
     */
    public abstract AnnotationMemberType getType( );
}