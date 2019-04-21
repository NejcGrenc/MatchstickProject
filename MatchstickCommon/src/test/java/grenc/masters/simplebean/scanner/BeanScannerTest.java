package grenc.masters.simplebean.scanner;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import grenc.masters.simplebean.annotation.Bean;

public class BeanScannerTest
{

	@Test
	public void shouldFindAnnotatedClass() throws ClassNotFoundException, IOException
	{
		Class<?>[] componentClasses = BeanScanner.scanPackage("grenc.masters").getClasses();
		assertTrue(containsClass(TestComponent.class, componentClasses));
	}
	
	private boolean containsClass(Class<?> expectedClass, Class<?>[] allClasses)
	{
		boolean contains = false;
		for (Class<?> c : allClasses)
			if (c.equals(expectedClass))
				contains = true;
		return contains;
	}
	
	@Bean
	private class TestComponent {}
}
