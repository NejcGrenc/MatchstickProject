package grenc.growscript.service.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import grenc.growscript.service.exception.GrowScriptException;
import grenc.growscript.service.handler.ClassProcessor;

public class ClassProcessorTest
{
	@Test
	public void shouldFindAllFieldNames()
	{
		List<String> allFieldNames = ClassProcessor.allFields(SimpleGrowClass.class);
		assertEquals(2, allFieldNames.size());
		assertTrue(allFieldNames.contains("i"));
		assertTrue(allFieldNames.contains("j"));
	}
	
	@Test
	public void shouldFindAllStringFieldsAsInterface()
	{
		List<String> fieldNames = ClassProcessor.allFieldsOfImplementing(SimpleGrowClass.class, SampleInterface.class);
		assertEquals(1, fieldNames.size());
		assertTrue(fieldNames.contains("j"));
	}
	
	@Test
	public void shouldFindAllStringFieldsAsImplementation()
	{
		List<String> fieldNames = ClassProcessor.allFieldsOfImplementing(DeclaredGrowClass.class, SampleInterface.class);
		assertEquals(1, fieldNames.size());
		assertTrue(fieldNames.contains("j"));
	}
	
	@Test
	public void shouldFindFieldByName() throws NoSuchFieldException, SecurityException
	{
		assertEquals(SimpleGrowClass.class.getDeclaredField("j"), ClassProcessor.findFieldByName(SimpleGrowClass.class, "j"));
	}
	
	@Test (expected = GrowScriptException.class)
	public void shouldThrowExceptionWhenFindFieldByName()
	{
		ClassProcessor.findFieldByName(SimpleGrowClass.class, "k");
	}
	
	
	
	@SuppressWarnings("unused")
	private class SimpleGrowClass
	{
		int i;
		SampleInterface j;
	}
	
	@SuppressWarnings("unused")
	private class DeclaredGrowClass
	{
		int i;
		ImplementingClass j;
	}

	private class ImplementingClass implements SampleInterface {}
	private interface SampleInterface {}
}

