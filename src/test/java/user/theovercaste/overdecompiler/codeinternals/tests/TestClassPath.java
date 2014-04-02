package user.theovercaste.overdecompiler.codeinternals.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;

public class TestClassPath {
	@Test
	public void testSingleArrayClass( ) {
		ClassPath p = new ClassPath("[Ljava/lang/Class");
		assertEquals("the depth of a single [ must be 1.", p.getArrayDepth(), 1);
	}

	@Test
	public void testMultipleArrayClass( ) {
		String basicClass = "java/lang/Class";
		String brackets = "[[[[[[[[[[";
		for (int i = 0; i < 10; i++) {
			ClassPath p = new ClassPath(brackets.substring(0, i) + "L" + basicClass);
			assertEquals("the depth of this array path must be " + i + ".", p.getArrayDepth(), i);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidClass( ) {
		new ClassPath("Class");
	}
}
