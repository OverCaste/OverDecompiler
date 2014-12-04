package user.theovercaste.overdecompiler.codeinternals.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import user.theovercaste.overdecompiler.util.ImportList;

public class TestImportList {
    @Test
    public void testListContains( ) {
        ImportList list = new ImportList();
        list.add("my.package.ClassName");
        list.add("my.packagewildcard.*");
        assertEquals("An import list must contain a directly added element.", true, list.contains("my.package.ClassName"));
        assertEquals("An import list must contain a single depth wildcard element.", true, list.contains("my.packagewildcard.TestClassName"));
        assertEquals("An import list must not contain any elements which are one node farther down from a wildcard node.", false, list.contains("my.packagewildcard.inner.TestClassName"));
        assertEquals("An import list must not contain any element which hasn't been added to it.", false, list.contains("unadded.node.ClassName"));
    }

    @Test
    public void testListRemove( ) {
        ImportList list = new ImportList();
        list.add("my.package.ClassName");
        list.add("my.package.*");
        list.remove("my.package.ClassName");
        list.remove("my.package.*");
        assertEquals("An import list must not contain elements which have been removed.", false, list.contains(list.contains("my.package.ClassName")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPeriodEndingAdditionException( ) {
        new ImportList().add("my.package.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingleNodeDepthException( ) {
        new ImportList().add("mynode");
    }
}
