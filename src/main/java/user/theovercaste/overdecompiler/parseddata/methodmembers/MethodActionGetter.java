package user.theovercaste.overdecompiler.parseddata.methodmembers;

import user.theovercaste.overdecompiler.util.ClassPath;

public abstract class MethodActionGetter extends MethodAction {
    @Override
    public boolean isGetter( ) {
        return true;
    }

    @Override
    public abstract ClassPath getClassType( );
}
