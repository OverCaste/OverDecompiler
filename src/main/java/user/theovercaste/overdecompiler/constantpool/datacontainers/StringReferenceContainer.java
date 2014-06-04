package user.theovercaste.overdecompiler.constantpool.datacontainers;

public class StringReferenceContainer extends IntegerContainer {
    public final int stringReference;

    public StringReferenceContainer(int nameIndex) {
        this.stringReference = nameIndex;
    }

    @Override
    protected int getValue( ) {
        return stringReference;
    }
}
