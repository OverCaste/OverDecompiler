package user.theovercaste.overdecompiler.constantpool.datacontainers;

import java.util.Objects;

public class InvokeDynamicContainer {
    public final int methodAttributeIndex;
    public final int nameAndTypeIndex;

    public InvokeDynamicContainer(int methodAttributeIndex, int nameAndTypeIndex) {
        this.methodAttributeIndex = methodAttributeIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(methodAttributeIndex, nameAndTypeIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        InvokeDynamicContainer other = (InvokeDynamicContainer) obj;
        return ((methodAttributeIndex == other.methodAttributeIndex) && (nameAndTypeIndex == other.nameAndTypeIndex));
    }
}
