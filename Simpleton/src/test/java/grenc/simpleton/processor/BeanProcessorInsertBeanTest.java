package grenc.simpleton.processor;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import grenc.simpleton.Beans;


public class BeanProcessorInsertBeanTest
{
	@Before
	public void cleanup()
	{
		Beans.removeAllBeans();
	}
	
	@Test
	public void shouldInsertBean() throws NoSuchFieldException, SecurityException
	{
		Beans.registerBean(BeanProcessor.createBean(InsertableTestClass1.class));
		
		Beans.registerBean(BeanProcessor.createBean(InjectIntoClass.class));
		Field field = InjectIntoClass.class.getDeclaredField("i");
		
		BeanProcessor.insertBeansIntoBeanForField(InjectIntoClass.class, field);
		
		assertTrue(((InjectIntoClass) Beans.getExact(InjectIntoClass.class)) != null);
		assertTrue(((InjectIntoClass) Beans.getExact(InjectIntoClass.class)).i != null);
		assertTrue(((InjectIntoClass) Beans.getExact(InjectIntoClass.class)).i.j == 5);
	}
		
}

class InsertableTestClass1 
{
	public int j = 5;
}

class InjectIntoClass
{
	public InsertableTestClass1 i;
}


