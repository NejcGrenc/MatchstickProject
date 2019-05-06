package grenc.simpleton.processor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import grenc.simpleton.Beans;
import grenc.simpleton.processor.exception.BeanProcessorException;
import grenc.simpleton.proxy.ProxyBeanProcessor;
import grenc.simpleton.scanner.BeanScanner;
import grenc.simpleton.scanner.InsertBeanScanner;
import grenc.simpleton.utils.SimpleLogger;

public class BeanProcessor
{

	public static void processPath(String path)
	{
		try
		{			
			Class<?>[] beanClasses = BeanScanner.scanPackage(path).getClasses();
			processClasses(beanClasses);
			
			ProxyBeanProcessor.processProxyBeans(path);
			
			SimpleLogger.printAllBeans();
			
			InsertBeanScanner scanner = InsertBeanScanner.scanPackage(path);
			for (Class<?> insertInto : scanner.getClasses())
				for (	Field field : scanner.getFields(insertInto))
					insertBeansIntoBeanForField(insertInto, field);
		
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			throw new BeanProcessorException("An exception occured, preventing proper execution.", e);
		}
	}
	
	
	protected static void processClasses(Class<?>[] classes)
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
	
	public static void insertBeansIntoBeanForField(Class<?> c, Field field)
	{
		Object recievingBeanInstance = Beans.getExact(c);
		field.setAccessible(true);
		
		Object insertedBeanInstance;
		try
		{
			insertedBeanInstance = BeanFinder.findBeanForType(field.getType());
		}
		catch (BeanProcessorException e)
		{
			e.printStackTrace();
			throw new BeanProcessorException("Exception occured while processing field [" + field.toString() + "].", e);
		}	
		
		try
		{
			field.set(recievingBeanInstance, insertedBeanInstance);
		} 
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
			throw new BeanProcessorException("Unable to insert bean [" + insertedBeanInstance.getClass() + "] into bean [" + c + "].", e);
		}

	}
	
	
	private static <B> void assertExactlyOneNoArgsConstructors(Class<B> beanClass)
	{
		int noArgsConstructors = 0;
		Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
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
