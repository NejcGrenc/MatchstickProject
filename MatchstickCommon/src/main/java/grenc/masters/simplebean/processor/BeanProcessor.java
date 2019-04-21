package grenc.masters.simplebean.processor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import grenc.masters.simplebean.Beans;
import grenc.masters.simplebean.processor.exception.BeanProcessorException;

public class BeanProcessor
{

	public static void processClasses(Class<?>[] classes)
	{
		for (Class<?> c : classes)
		{
			Object bean = createBean(c);
			Beans.registerBean(bean);
		}
	}
	
	public static <B> B createBean(Class<B> beanClass)
	{
		try
		{
			assertExactlyOneNoArgsConstructors(beanClass);
			
			Constructor<B> constructor = beanClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
			throw new BeanProcessorException("Unhandled exception", e);
		}
	}
	
	
	private static <B> void assertExactlyOneNoArgsConstructors(Class<B> beanClass)
	{
		int noArgsConstructors = 0;
		Constructor<?>[] constructors =  TestClass.class.getDeclaredConstructors();
		for (Constructor<?> c : constructors)
		{
			if (c.getParameterCount() == 0)
				noArgsConstructors++;
		}
		
		if (noArgsConstructors == 0)
		{
			String possibleError = "No no-args constructors found (not even default ones) for class ["
					+ beanClass.getCanonicalName() + "]. "
					+ "It is possible that there is not any constructor that takes 0 arguments "
					+ "or that the class is itself a subclass of another class. This will not work.";
			throw new BeanProcessorException(possibleError);
		}
		if (noArgsConstructors > 1)
		{
			throw new BeanProcessorException("More than one no-args constructor for class [" + beanClass.getCanonicalName() + "]." );
		}
	}
	
	
}
