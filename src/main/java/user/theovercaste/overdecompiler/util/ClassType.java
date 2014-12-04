package user.theovercaste.overdecompiler.util;

public enum ClassType {
    /** Classes defined with the @interface header. These are used by annotation processors to give little side metadata to classes. */
    ANNOTATION,
    /** Classes defined with the enum header. They have a set of immutable fields initialized at runtime, usually meant to give a list of similar elements. */
    ENUM,
    /** Regular classes, which may contain any number of fields, methods, and subclasses */
    CLASS,
    /** Abstract classes, which can't be instantiated by themselves. Subclasses extend them to make use of shared methods and fields. */
    ABSTRACT_CLASS,
    /** Interfaces, which define a contract of methods to be followed by any class which follow them. */
    INTERFACE
}
