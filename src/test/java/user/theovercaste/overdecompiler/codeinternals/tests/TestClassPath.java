package user.theovercaste.overdecompiler.codeinternals.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import user.theovercaste.overdecompiler.codeinternals.ClassPath;

public class TestClassPath {
	@Test
	public void testSingleArrayClass( ) {
		ClassPath p = ClassPath.getMangledPath("[Ljava/lang/Class");
		assertEquals("The depth of a single [ must be 1.", 1, p.getArrayDepth());
		assertEquals("The package should be java.lang", "java.lang", p.getClassPackage());
		assertEquals("The class name should be Class", "Class", p.getClassName());
	}

	@Test
	public void testMultipleArrayClass( ) {
		String basicClass = "java/lang/Class";
		String brackets = "[[[[[[[[[[";
		for (int i = 0; i < 10; i++) {
			ClassPath p = ClassPath.getMangledPath(brackets.substring(0, i) + "L" + basicClass);
			assertEquals("The depth of this array path must be " + i + ".", p.getArrayDepth(), i);
		}
	}

	@Test
	public void testMethodClasspath( ) {
		String methodDeclaration = "(Ljava/lang/String;Ljava/lang/Class)V";
		Collection<ClassPath> arguments = ClassPath.getMethodArguments(methodDeclaration);
		assertNotNull(arguments);
		List<ClassPath> argumentsList = new ArrayList<ClassPath>(arguments.size());
		argumentsList.addAll(arguments);
		assertEquals("The size of arguments must be 2.", 2, arguments.size());
		ClassPath arg = argumentsList.get(0);
		assertEquals("The package of the first argument should be java.lang", "java.lang", arg.getClassPackage());
		assertEquals("The class of the first argument should be String", "String", arg.getClassName());
		arg = argumentsList.get(1);
		assertEquals("The package of the second argument should be java.lang", "java.lang", arg.getClassPackage());
		assertEquals("The class of the second argument should be Class", "Class", arg.getClassName());
		assertEquals("The return type should be void", "void", ClassPath.getMethodReturnType(methodDeclaration).getClassName());
	}
}
