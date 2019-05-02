package grenc.growscript.processor;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import grenc.growscript.processor.ClassProcessor;

public class ClassProcessorTest
{
	@Test
	public void shouldFindAllFields()
	{
		List<String> allFieldNames = ClassProcessor.allFields(SimpleGrowClass.class);
		assertTrue(allFieldNames.size() == 2);
		assertTrue(allFieldNames.contains("i"));
		assertTrue(allFieldNames.contains("j"));
	}
	
	@Test
	public void shouldFindAllStringFields()
	{
		List<String> fieldNames = ClassProcessor.allFieldsOfType(SimpleGrowClass.class, String.class);
		assertTrue(fieldNames.size() == 1);
		assertTrue(fieldNames.contains("j"));
	}
}

class SimpleGrowClass
{
	int i;
	String j;
}
