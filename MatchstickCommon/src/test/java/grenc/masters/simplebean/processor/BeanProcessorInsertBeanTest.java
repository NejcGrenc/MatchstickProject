package grenc.masters.simplebean.processor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import grenc.masters.simplebean.Beans;
import grenc.masters.simplebean.processor.exception.BeanProcessorException;


public class BeanProcessorInsertBeanTest
{
	@Before
	public void cleanup()
	{
		Beans.removeAllBeans();
	}

	
	@Test
	public void shouldFindInsertableBean() throws NoSuchFieldException, SecurityException
	{
		Beans.registerBean(BeanProcessor.createBean(InsertableTestClass1.class));
		
		Class<?> bean = BeanProcessor.applicableBean(InjectIntoClass.class.getDeclaredField("i"));
		assertTrue(bean == InsertableTestClass1.class);
	}
	
	@Test(expected = BeanProcessorException.class)
	public void shouldFailToInsertIfNoApplicableBeanRegistered() throws NoSuchFieldException, SecurityException
	{
		Beans.registerBean(BeanProcessor.createBean(InsertableTestClass2.class));
		
		BeanProcessor.applicableBean(InjectIntoClass.class.getDeclaredField("i"));
		fail();
	}
	
	@Test(expected = BeanProcessorException.class)
	public void shouldFailToInsertIfMultipleApplicableBeanRegistered() throws NoSuchFieldException, SecurityException
	{
		Beans.registerBean(BeanProcessor.createBean(InsertableTestClass1.class));
		Beans.registerBean(BeanProcessor.createBean(InsertableTestClassExtended1.class));
		
		BeanProcessor.applicableBean(InjectIntoClass.class.getDeclaredField("i"));
		fail();
	}
	
	
	@Test
	public void shouldInsertBean() throws NoSuchFieldException, SecurityException
	{
		Beans.registerBean(BeanProcessor.createBean(InsertableTestClass1.class));
		
		Beans.registerBean(BeanProcessor.createBean(InjectIntoClass.class));
		Field field = InjectIntoClass.class.getDeclaredField("i");
		
		BeanProcessor.insertBeansIntoBeanForField(InjectIntoClass.class, field);
		
		assertTrue(((InjectIntoClass) Beans.get(InjectIntoClass.class)) != null);
		assertTrue(((InjectIntoClass) Beans.get(InjectIntoClass.class)).i != null);
		assertTrue(((InjectIntoClass) Beans.get(InjectIntoClass.class)).i.j == 5);
	}
		
}

class InsertableTestClass1 
{
	public int j = 5;
}

class InsertableTestClass2 {}

class InsertableTestClassExtended1 extends InsertableTestClass1 {}

class InjectIntoClass
{
	public InsertableTestClass1 i;
}


