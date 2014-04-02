package user.theovercaste.overdecompiler.datahandlers;

public class MethodFlagHandler {
	private boolean flagPublic; // flag prefixes because these are all keywords. Go figure.
	private boolean flagPrivate;
	private boolean flagProtected;
	private boolean flagStatic;
	private boolean flagFinal;
	private boolean flagSynchronized;
	private boolean flagBridge;
	private boolean flagVarargs;
	private boolean flagNative;
	private boolean flagAbstract;
	private boolean flagStrict;
	private boolean flagSynthetic;

	public MethodFlagHandler(int value) {
		this.flagPublic = (value & 0x0001) != 0;
		this.flagPrivate = (value & 0x0002) != 0;
		this.flagProtected = (value & 0x0004) != 0;
		this.flagStatic = (value & 0x0008) != 0;
		this.flagFinal = (value & 0x0010) != 0;
		this.flagSynchronized = (value & 0x0020) != 0;
		this.flagBridge = (value & 0x0040) != 0;
		this.flagVarargs = (value & 0x0080) != 0;
		this.flagNative = (value & 0x0100) != 0;
		this.flagAbstract = (value & 0x0400) != 0;
		this.flagStrict = (value & 0x0800) != 0;
		this.flagSynthetic = (value & 0x1000) != 0;
	}

	public boolean isPublic( ) {
		return this.flagPublic;
	}

	public boolean isPrivate( ) {
		return this.flagPrivate;
	}

	public boolean isProtected( ) {
		return this.flagProtected;
	}

	public boolean isStatic( ) {
		return this.flagStatic;
	}

	public boolean isFinal( ) {
		return this.flagFinal;
	}

	public boolean isSynchronized( ) {
		return this.flagSynchronized;
	}

	public boolean isBridge( ) {
		return this.flagBridge;
	}

	public boolean isVarargs( ) {
		return this.flagVarargs;
	}

	public boolean isNative( ) {
		return this.flagNative;
	}

	public boolean isAbstract( ) {
		return this.flagAbstract;
	}

	public boolean isStrict( ) {
		return this.flagStrict;
	}

	public boolean isSynthetic( ) {
		return this.flagSynthetic;
	}
}
