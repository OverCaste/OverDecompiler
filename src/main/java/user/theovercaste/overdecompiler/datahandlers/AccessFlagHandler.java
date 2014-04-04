package user.theovercaste.overdecompiler.datahandlers;

public class AccessFlagHandler {
	private boolean flagPublic; // flag prefixes because these are all keywords. Go figure.
	private boolean flagFinal;
	private boolean flagSuper;
	private boolean flagInterface;
	private boolean flagAbstract;
	private boolean flagSynthetic;
	private boolean flagAnnotation;
	private boolean flagEnum;

	public AccessFlagHandler(int value) {
		flagPublic = ((value & 0x0001) != 0);
		flagFinal = ((value & 0x0010) != 0);
		flagSuper = ((value & 0x0020) != 0);
		flagInterface = ((value & 0x0200) != 0);
		flagAbstract = ((value & 0x0400) != 0);
		flagSynthetic = ((value & 0x1000) != 0);
		flagAnnotation = ((value & 0x2000) != 0);
		flagEnum = ((value & 0x4000) != 0);
	}

	public AccessFlagHandler( ) {
		this(0);
	}

	public void setPublic(boolean flagPublic) {
		this.flagPublic = flagPublic;
	}

	public void setFinal(boolean flagFinal) {
		this.flagFinal = flagFinal;
	}

	public void setSuper(boolean flagSuper) {
		this.flagSuper = flagSuper;
	}

	public void setInterface(boolean flagInterface) {
		this.flagInterface = flagInterface;
	}

	public void setAbstract(boolean flagAbstract) {
		this.flagAbstract = flagAbstract;
	}

	public void setSynthetic(boolean flagSynthetic) {
		this.flagSynthetic = flagSynthetic;
	}

	public void setAnnotation(boolean flagAnnotation) {
		this.flagAnnotation = flagAnnotation;
	}

	public void setEnum(boolean flagEnum) {
		this.flagEnum = flagEnum;
	}

	public boolean isPublic( ) {
		return flagPublic;
	}

	public boolean isFinal( ) {
		return flagFinal;
	}

	public boolean isSuper( ) {
		return flagSuper;
	}

	public boolean isInterface( ) {
		return flagInterface;
	}

	public boolean isAbstract( ) {
		return flagAbstract;
	}

	public boolean isSynthetic( ) {
		return flagSynthetic;
	}

	public boolean isAnnotation( ) {
		return flagAnnotation;
	}

	public boolean isEnum( ) {
		return flagEnum;
	}

	@Override
	public String toString( ) {
		return "AccessFlagHandler [flagPublic=" + flagPublic + ", flagFinal=" + flagFinal + ", flagSuper=" + flagSuper + ", flagInterface=" + flagInterface + ", flagAbstract=" + flagAbstract
				+ ", flagSynthetic=" + flagSynthetic + ", flagAnnotation=" + flagAnnotation + ", flagEnum=" + flagEnum + "]";
	}
}
