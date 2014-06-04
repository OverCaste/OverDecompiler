package user.theovercaste.overdecompiler.constantpool.datacontainers;

public abstract class IntegerContainer {
    protected abstract int getValue( );

    @Override
    public int hashCode( ) {
        return getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        IntegerContainer other = (IntegerContainer) obj;
        return getValue() == other.getValue();
    }
}
