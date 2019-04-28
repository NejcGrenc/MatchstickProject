package grenc.simpleton.scanner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

import grenc.simpleton.annotation.Bean;
import grenc.simpleton.scanner.ClassLoader;

public class ClassLoaderTest
{

	@Test
	public void shouldFindAllClasses() throws ClassNotFoundException, IOException
	{
		Class<?>[] allClasses = ClassLoader.getClasses("grenc.simpleton.scanner");
		for (Class<?> c : allClasses)
			System.out.println(c.getName());
	}
	
	@Test
	public void shouldScannOnlySpecifidPackage() throws ClassNotFoundException, IOException
	{
		Class<?>[] allClasses = ClassLoader.getClasses("grenc.simpleton.scanner");
		assertContainsClass(ClassLoaderTest.class, allClasses);
		assertDoesNotContainClass(Bean.class, allClasses);
	}
	
	@Test
	public void shouldFindItself() throws ClassNotFoundException, IOException 
	{
		Class<?>[] allClasses = ClassLoader.getClasses("grenc.simpleton");
		assertContainsClass(ClassLoader.class, allClasses);
	}
	
	@Test
	public void shouldFindTestClass() throws ClassNotFoundException, IOException 
	{
		Class<?>[] allClasses = ClassLoader.getClasses("grenc.simpleton");
		assertContainsClass(ClassLoaderTest.class, allClasses);
	}
	
	@Test
	public void shouldFindNonPublicClasses() throws ClassNotFoundException, IOException 
	{
		Class<?>[] allClasses = ClassLoader.getClasses("grenc.simpleton.scanner");
		assertContainsClass(TestInnerClass.class, allClasses);
		assertContainsClass(OuterTestClass.class, allClasses);
	}
	
	
	private void assertContainsClass(Class<?> expectedClass, Class<?>[] allClasses)
	{
		assertTrue(containsClass(expectedClass, allClasses));	
	}
	
	private void assertDoesNotContainClass(Class<?> expectedClass, Class<?>[] allClasses)
	{
		assertFalse(containsClass(expectedClass, allClasses));	
	}
	
	private boolean containsClass(Class<?> expectedClass, Class<?>[] allClasses)
	{
		boolean contains = false;
		for (Class<?> c : allClasses)
			if (c.equals(expectedClass))
				contains = true;
		return contains;
	}
	

	private class TestInnerClass {}
}

class OuterTestClass {}

