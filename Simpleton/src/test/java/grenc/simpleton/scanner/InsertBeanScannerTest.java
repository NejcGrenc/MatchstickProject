package grenc.simpleton.scanner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Test;

import grenc.simpleton.annotation.InsertBean;
import grenc.simpleton.scanner.InsertBeanScanner;

public class InsertBeanScannerTest
{

	@Test
	public void shouldFindAnnotatedFields() throws ClassNotFoundException, IOException, NoSuchFieldException, SecurityException
	{
		Field[] fields = InsertBeanScanner.scanPackage("grenc.simpleton").getAllFields();
		assertTrue(containsField(TestClass.class.getDeclaredField("i"), fields));
		assertTrue(containsField(TestClass.class.getDeclaredField("j"), fields));
		assertFalse(containsField(TestClass.class.getDeclaredField("k"), fields));
	}
	
	private boolean containsField(Field expectedField, Field[] allFields)
	{
		boolean contains = false;
		for (Field c : allFields)
			if (c.equals(expectedField))
				contains = true;
		return contains;
	}
	
}

class TestClass
{
	@InsertBean
	int i;
	@InsertBean
	int j;
	int k;
}
