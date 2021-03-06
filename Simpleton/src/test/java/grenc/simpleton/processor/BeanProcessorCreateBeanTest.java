package grenc.simpleton.processor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import grenc.simpleton.Beans;
import grenc.simpleton.processor.exception.BeanProcessorException;


public class BeanProcessorCreateBeanTest
{
	@Before
	public void cleanup()
	{
		Beans.removeAllBeans();
	}

	
	@Test
	public void shouldCreateObject()
	{
		TestClass instance = BeanProcessor.createBean(TestClass.class);
		assertTrue(instance != null);
		assertTrue(instance instanceof TestClass);
	}
	
	@Test
	public void shouldCreateObjectByNoArgsConstructor()
	{
		TestClassWithConstructor instance = BeanProcessor.createBean(TestClassWithConstructor.class);
		assertTrue(instance != null);
		assertTrue(instance instanceof TestClassWithConstructor);
		assertTrue(instance.i == 5);
	}
	
	@Test (expected = BeanProcessorException.class)
	public void shouldFailToCreateSubclassedObject()
	{
		BeanProcessor.createBean(PrivateTestClassForFailure.class);
		fail();
	}
	
	@Test (expected = BeanProcessorException.class)
	public void shouldFailToCreateObjectWithOnlyArgsConstructor()
	{
		BeanProcessor.createBean(TestClassWithArgsConstructorForFailure.class);
		fail();
	}
	
	@Test (expected = BeanProcessorException.class)
	public void shouldFailToCreateAlreadyExistingBean()
	{
		Beans.registerBean(BeanProcessor.createBean(TestClass.class));
		Beans.registerBean(BeanProcessor.createBean(TestClass.class));
		fail();
	}
	
	
	@Test
	public void shouldCreateRequiredBeans()
	{
		Class<?>[] beansToMake = new Class<?>[] {TestClass.class, TestClassWithConstructor.class};
		BeanProcessor.processClasses(beansToMake);
		
		assertTrue(Beans.getExact(TestClass.class) != null);
		assertTrue(Beans.getExact(TestClassWithConstructor.class) != null);
		
		try {
			Beans.getExact(TestClass2.class);
			fail("Bean TestClass2.class is not expected to be registered, but it somehow is.");
		} catch (BeanProcessorException e) {
			// Exception is thrown in case bean is not found
		}
	}
	
	

	class PrivateTestClassForFailure {}
}

class TestClass {}

class TestClass2 {}

class TestClassWithConstructor
{
	public int i;
	public TestClassWithConstructor() { this.i=5; }
}

class TestClassWithArgsConstructorForFailure
{
	public int i;
	public TestClassWithArgsConstructorForFailure(int a) { this.i=a; }
}

