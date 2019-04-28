package grenc.simpleton.processor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import grenc.simpleton.Beans;
import grenc.simpleton.processor.exception.BeanProcessorException;
import grenc.simpleton.proxy.ProxyBeanProcessor;
import grenc.simpleton.scanner.BeanScanner;
import grenc.simpleton.scanner.InsertBeanScanner;

public class BeanProcessor
{

	public static void processPath(String path)
	{
		try
		{
			Class<?>[] beanClasses = BeanScanner.scanPackage(path).getClasses();
			processClasses(beanClasses);
			
			ProxyBeanProcessor.processProxyBeans(path);
			
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
		Object recievingBeanInstance = Beans.get(c);
		field.setAccessible(true);
		
		Class<?> beanType = applicableBean(field);
		Object insertedBeanInstance = Beans.get(beanType);
		
		try
		{
			field.set(recievingBeanInstance, insertedBeanInstance);
		} 
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
			throw new BeanProcessorException("Unable to insert bean [" + beanType + "] into bean [" + c + "].", e);
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
	
	
	protected static Class<?> applicableBean(Field field)
	{
		List<Class<?>> applicableBeans = new ArrayList<>();
		for (Class<?> beanClass : Beans.allRegisteredTypes())
		{
			if (isApplicable(field, beanClass))
				applicableBeans.add(beanClass);
		}
			
		if (applicableBeans.isEmpty())
			throw new BeanProcessorException("No applicable bean found for field [" + field.toString() + "]");
		
		if (applicableBeans.size() > 1)
		{	
			String beans = "";
			for (Class<?> bean : applicableBeans)
				beans += bean.getName() + " ";
			throw new BeanProcessorException("Too many applicable bean found for field [" + field.toString() + "] : " + beans);
		}
		
		return applicableBeans.get(0);
	}
	
	private static boolean isApplicable(Field field, Class<?> c)
	{
		if (field.getType().equals(c))
			return true;
		
		boolean applicable = false;
		for (Class<?> interf : c.getInterfaces())
			applicable = (applicable) ? true : isApplicable(field, interf);
		if (c.getSuperclass() != null)
			applicable = (applicable) ? true : isApplicable(field, c.getSuperclass());
		
		return applicable;
	}
	
}
