package user.theovercaste.overdecompiler.constantpool.datacontainers;

public class ClassContainer extends IntegerContainer {
    public final int nameIndex;

    public ClassContainer(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    @Override
    protected int getValue( ) {
        return nameIndex;
    }
}
