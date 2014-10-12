package user.theovercaste.overdecompiler.codeinternals;

public enum ClassFlag {
    PUBLIC(0x0001),
    FINAL(0x0010),
    SUPER(0x0020),
    INTERFACE(0x0200),
    ABSTRACT(0x0400),
    SYNTHETIC(0x1000),
    ANNOTATION(0x2000),
    ENUM(0x4000);

    private final int opcode;

    ClassFlag(int opcode) {
        this.opcode = opcode;
    }

    public int getOpcode( ) {
        return opcode;
    }
}
