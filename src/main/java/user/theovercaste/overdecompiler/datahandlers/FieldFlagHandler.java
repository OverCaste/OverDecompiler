package user.theovercaste.overdecompiler.datahandlers;

public class FieldFlagHandler {
	private boolean flagPublic; // flag prefixes because these are all keywords. Go figure.
	private boolean flagPrivate;
	private boolean flagProtected;
	private boolean flagStatic;
	private boolean flagFinal;
	private boolean flagVolatile;
	private boolean flagTransient;
	private boolean flagSynthetic;
	private boolean flagEnum;

	public FieldFlagHandler(int value) {
		flagPublic = (value & 0x0001) != 0;
		flagPrivate = (value & 0x0002) != 0;
		flagProtected = (value & 0x0004) != 0;
		flagStatic = (value & 0x0008) != 0;
		flagFinal = (value & 0x0010) != 0;
		flagVolatile = (value & 0x0040) != 0;
		flagTransient = (value & 0x0080) != 0;
		flagSynthetic = (value & 0x1000) != 0;
		flagEnum = (value & 0x4000) != 0;
		System.out.println("Field flags: " + Integer.toHexString(value) + ", " + toString());
	}

	public boolean isPublic( ) {
		return flagPublic;
	}

	public boolean isPrivate( ) {
		return flagPrivate;
	}

	public boolean isProtected( ) {
		return flagProtected;
	}

	public boolean isStatic( ) {
		return flagStatic;
	}

	public boolean isFinal( ) {
		return flagFinal;
	}

	public boolean isVolatile( ) {
		return flagVolatile;
	}

	public boolean isTransient( ) {
		return flagTransient;
	}

	public boolean isSynthetic( ) {
		return flagSynthetic;
	}

	public boolean isEnum( ) {
		return flagEnum;
	}

	@Override
	public String toString( ) {
		return "FieldFlagHandler [flagPublic=" + flagPublic + ", flagPrivate=" + flagPrivate + ", flagProtected=" + flagProtected + ", flagStatic=" + flagStatic + ", flagFinal=" + flagFinal
				+ ", flagVolatile=" + flagVolatile + ", flagTransient=" + flagTransient + ", flagSynthetic=" + flagSynthetic + ", flagEnum=" + flagEnum + "]";
	}
}
