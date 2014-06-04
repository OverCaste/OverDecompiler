package user.theovercaste.overdecompiler.constantpool.datacontainers;

import java.util.Objects;

public class NameAndTypeContainer {
    public final int nameIndex;
    public final int descriptorIndex;

    public NameAndTypeContainer(int nameIndex, int descriptorIndex) {
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    @Override
    public int hashCode( ) {
        return Objects.hash(nameIndex, descriptorIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        NameAndTypeContainer other = (NameAndTypeContainer) obj;
        return ((nameIndex == other.nameIndex) && (descriptorIndex == other.descriptorIndex));
    }
}
