package user.theovercaste.overdecompiler.util;

import java.util.Arrays;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;

public enum JavaVersion {
    UNKNOWN(0, "Unknown Java Version", 0),
    JAVA_1(45, "JDK 1.1", 1),
    JAVA_2(46, "JDK 1.2", 2),
    JAVA_3(47, "JDK 1.3", 3),
    JAVA_4(48, "JDK 1.4", 4),
    JAVA_5(49, "J2SE 5.0", 5),
    JAVA_6(50, "J2SE 6.0", 6),
    JAVA_7(51, "J2SE 7", 7),
    JAVA_8(52, "J2SE 8", 8);

    private static final ImmutableBiMap<JavaVersion, Integer> INTERNAL_ID_MAP = ImmutableBiMap.copyOf(Maps.toMap(Arrays.asList(values()), new Function<JavaVersion, Integer>() {
        @Override
        public Integer apply(JavaVersion input) {
            return input.internalMajorVersion;
        }
    }));

    private static final ImmutableBiMap<JavaVersion, Integer> JAVA_VERSION_MAP = ImmutableBiMap.copyOf(Maps.toMap(Arrays.asList(values()), new Function<JavaVersion, Integer>() {
        @Override
        public Integer apply(JavaVersion input) {
            return input.javaVersion;
        }
    }));

    private final int internalMajorVersion;
    private final String identifier;
    private final int javaVersion;

    private JavaVersion(int internalMajorVersion, String identifier, int javaVersion) {
        this.internalMajorVersion = internalMajorVersion;
        this.identifier = identifier;
        this.javaVersion = javaVersion;
    }

    public int getInternalMajorVersion( ) {
        return internalMajorVersion;
    }

    public String getIdentifier( ) {
        return identifier;
    }

    public int getJavaVersion( ) {
        return javaVersion;
    }

    @Override
    public String toString( ) {
        return getIdentifier();
    }

    /**
     * Checks whether a JavaVersion exists for the corresponding integer.
     * 
     * @param internalJavaId the integer representation of the JavaVersion to be checked for existence, for example one retrieved from the header of a Java class file.
     * @return whether or not the specified internal version corresponds to a known Java version.
     * 
     * @see #getByInternalVersion(int)
     */
    public static boolean checkInternalVersionExists(int internalJavaId) {
        if (internalJavaId == 0) {
            return false;
        }
        return INTERNAL_ID_MAP.containsValue(internalJavaId);
    }

    /**
     * Retrieves a JavaVersion from the specified internal Java id.
     * 
     * @param internalJavaId the integer representation of the JavaVersion to be retrieved, for example one retrieved from the header of a Java class file.
     * @return the JavaVersion representation of the specified internal java id.
     * 
     * @see #checkInternalVersionExists(int)
     * @see #getByJavaVersion(int)
     */
    public static JavaVersion getByInternalVersion(int internalJavaId) {
        Preconditions.checkArgument(internalJavaId != 0, "id 0 is reserved for the unknown java version value.");
        return INTERNAL_ID_MAP.inverse().get(internalJavaId);
    }

    /**
     * Checks whether a JavaVersion is known to exist for the specified Java version.
     * 
     * @param javaVersion the Java version to be checked, for example Java 7's version number would be 7.
     * @return the JavaVersion representation of the specified java version.
     * 
     * @see #getByJavaVersion(int)
     */
    public static boolean checkJavaVersionExists(int javaVersion) {
        if (javaVersion == 0) {
            return false;
        }
        return JAVA_VERSION_MAP.containsValue(javaVersion);
    }

    /**
     * Retrieves a JavaVersion from the specified Java version.
     * 
     * @param javaVersion the Java version to be retrieved, for example Java 7's version number would be 7.
     * @return the corresponding JavaVersion.
     * 
     * @see #checkJavaVersionExists(int)
     * @see #getByInternalVersion(int)
     */
    public static JavaVersion getByJavaVersion(int javaVersion) {
        Preconditions.checkArgument(javaVersion != 0, "version 0 is reserved for the unknown java version value.");
        return JAVA_VERSION_MAP.inverse().get(javaVersion);
    }
}
