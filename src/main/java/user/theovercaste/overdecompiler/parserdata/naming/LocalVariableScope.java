package user.theovercaste.overdecompiler.parserdata.naming;

public enum LocalVariableScope {
	/** Any variable declared as a parameter. */
	PARAMETER,
	/** Any variable declared in the direct method body. */
	METHOD_BODY,
	/** Any variable declared in a switch with brackets. (switch(value); case 0: {int foo = bar}) */
	SWITCH_BODY,
	/** Any variable declared in an if statement. */
	IF_BODY,
	/** Any variable declared in a for loop header. */
	FOR_LOOP_HEADER,
	/** Any variable declared in a for loop body. */
	FOR_LOOP_BODY,
	/** Any variable declared in a while loop body */
	WHILE_LOOP_BODY,
	/** Any variable declared in a do while loop body */
	DO_WHILE_LOOP_BODY;
}
