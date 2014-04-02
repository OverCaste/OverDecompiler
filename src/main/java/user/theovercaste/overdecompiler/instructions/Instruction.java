package user.theovercaste.overdecompiler.instructions;

import java.io.DataInputStream;
import java.io.IOException;

public abstract class Instruction {
	public static abstract class Factory {
		public abstract Instruction load(DataInputStream din) throws IOException;
	}
}
