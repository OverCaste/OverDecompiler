package user.theovercaste.overdecompiler.codeinternals;

/**
 * This represents anything that refines the variable scope.
 * 
 * @author <a href="http://www.reddit.com/user/TheOverCaste/">OverCaste</a>
 *
 */
public class CodeBlock {
	protected final CodeBlockType type;

	public CodeBlock(CodeBlockType type) {
		this.type = type;
	}

	public CodeBlockType getType( ) {
		return type;
	}
}
