package user.theovercaste.overdecompiler.constantpool.datacontainers;

import java.util.Objects;

public class MethodReferenceContainer {
    public final int classIndex;
    public final int nameAndTypeIndex;

    public MethodReferenceContainer(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(classIndex, nameAndTypeIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        MethodReferenceContainer other = (MethodReferenceContainer) obj;
        return ((classIndex == other.classIndex) && (nameAndTypeIndex == other.nameAndTypeIndex));
    }
}
