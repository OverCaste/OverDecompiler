package user.theovercaste.overdecompiler.codeinternals.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;

public class TestClassPath {
	@Test
	public void testSingleArrayClass( ) {
		ClassPath p = ClassPath.getMangledPath("[Ljava/lang/Class");
		assertEquals("the depth of a single [ must be 1.", 1, p.getArrayDepth());
		assertEquals("the package should be java.lang", "java.lang", p.getClassPackage());
		assertEquals("the class name should be Class", "Class", p.getClassName());
	}

	@Test
	public void testMultipleArrayClass( ) {
		String basicClass = "java/lang/Class";
		String brackets = "[[[[[[[[[[";
		for (int i = 0; i < 10; i++) {
			ClassPath p = ClassPath.getMangledPath(brackets.substring(0, i) + "L" + basicClass);
			assertEquals("the depth of this array path must be " + i + ".", p.getArrayDepth(), i);
		}
	}
}
