package user.theovercaste.overdecompiler.constantpool.datacontainers;

import java.util.Objects;

public class FieldReferenceContainer {
    public final int classIndex;
    public final int nameAndTypeIndex;

    public FieldReferenceContainer(int classIndex, int nameAndTypeIndex) {
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
        FieldReferenceContainer other = (FieldReferenceContainer) obj;
        return ((classIndex == other.classIndex) && (nameAndTypeIndex == other.nameAndTypeIndex));
    }
}
