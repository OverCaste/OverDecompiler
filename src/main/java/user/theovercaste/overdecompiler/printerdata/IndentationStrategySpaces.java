package user.theovercaste.overdecompiler.printerdata;

import java.util.Arrays;

import com.google.common.base.Preconditions;

public class IndentationStrategySpaces implements IndentationStrategy {
    private static final char SPACE_CHARACTER = ' ';
    
    private final int spaceCount;
    
    public IndentationStrategySpaces(int spaceCount) {
        Preconditions.checkArgument(spaceCount >= 0, "space count must be greater or equal to 0.");
        this.spaceCount = spaceCount;
    }
    
    @Override
    public String getIndentation(int indentationLevel) {
        Preconditions.checkArgument(indentationLevel >= 0, "indentation level must be greater or equal to 0.");
        char[] spaceBuffer = new char[indentationLevel*spaceCount];
        Arrays.fill(spaceBuffer, SPACE_CHARACTER);
        return new String(spaceBuffer);
    }
}
