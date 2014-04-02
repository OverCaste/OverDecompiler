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
		this.flagPublic = ((value & 0x0001) != 0);
		this.flagFinal = ((value & 0x0010) != 0);
		this.flagSuper = ((value & 0x0020) != 0);
		this.flagInterface = ((value & 0x0200) != 0);
		this.flagAbstract = ((value & 0x0400) != 0);
		this.flagSynthetic = ((value & 0x1000) != 0);
		this.flagAnnotation = ((value & 0x2000) != 0);
		this.flagEnum = ((value & 0x4000) != 0);
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
		return this.flagPublic;
	}

	public boolean isFinal( ) {
		return this.flagFinal;
	}

	public boolean isSuper( ) {
		return this.flagSuper;
	}

	public boolean isInterface( ) {
		return this.flagInterface;
	}

	public boolean isAbstract( ) {
		return this.flagAbstract;
	}

	public boolean isSynthetic( ) {
		return this.flagSynthetic;
	}

	public boolean isAnnotation( ) {
		return this.flagAnnotation;
	}

	public boolean isEnum( ) {
		return this.flagEnum;
	}
}
