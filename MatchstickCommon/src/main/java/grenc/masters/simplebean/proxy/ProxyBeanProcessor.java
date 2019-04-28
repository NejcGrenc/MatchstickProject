package grenc.masters.simplebean.proxy;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import grenc.masters.simplebean.Beans;
import grenc.masters.simplebean.processor.exception.BeanProcessorException;
import grenc.masters.simplebean.proxy.annotation.ProxyBean;


public class ProxyBeanProcessor
{

	public static void processProxyBeans(String packageName) throws ClassNotFoundException, IOException
	{
		Class<?>[] classesToBeProxies = ProxyBeanScanner.scanPackage(packageName).getClasses();
		
		for (Class<?> originalClass : classesToBeProxies)
		{
			Object originalBean = popOriginalBean(originalClass);
			Object proxyBean = createProxyBean(originalBean);
			Beans.registerBeanAs(proxyBean, implementedInterface(originalClass));
		}
	}
	
	private static Object popOriginalBean(Class<?> originalClass)
	{
		Object originalBeanInstance = Beans.get(originalClass);
		Beans.removeBean(originalClass);
		return originalBeanInstance;
	}
	
	
	static Object createProxyBean(Object originalBeanInstance)
	{
		Class<?> originalClass = originalBeanInstance.getClass();
		Class<?> proxyClass = proxyClass(originalClass);
		Class<?> implementedInterface = implementedInterface(originalClass);
		
		return Proxy.newProxyInstance(
					ProxyBeanProcessor.class.getClassLoader(), 
					new Class[] { implementedInterface }, 
					createInvocationHandler(proxyClass, originalBeanInstance)
				);
	}
	
	
	private static Class<?> proxyClass(Class<?> originalClass)
	{
		ProxyBean proxyAnnotation = originalClass.getAnnotation(ProxyBean.class);
		return proxyAnnotation.proxyClass();
	}
	
	private static Class<?> implementedInterface(Class<?> originalClass)
	{
		Class<?>[] interfaces = originalClass.getInterfaces();
		if (interfaces.length == 0)
			throw new BeanProcessorException("Problem creating proxy: No interface found for class [" + originalClass + "].");
		if (interfaces.length > 1)
			throw new BeanProcessorException("Problem creating proxy: More than one interface found for class [" + originalClass + "].");
		
		return interfaces[0];
	}
	
	private static InvocationHandler createInvocationHandler(Class<?> proxyClass, Object originalBeanInstance)
	{
		try
		{
			Constructor<?> constructor = getOneArgumentConstructor(proxyClass);
			constructor.setAccessible(true);
			
			return (InvocationHandler) constructor.newInstance(originalBeanInstance);
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e)
		{
			e.printStackTrace();
			throw new BeanProcessorException("Unable to instanciate proxy bean [" + proxyClass + "].", e);
		}
	}

	
	private static Constructor<?> getOneArgumentConstructor(Class<?> targetClass)
	{
		List<Constructor<?>> oneArgsConstructors = new ArrayList<>();
		Constructor<?>[] constructors = targetClass.getDeclaredConstructors();
		for (Constructor<?> c : constructors)
		{
			if (c.getParameterCount() == 1)
				oneArgsConstructors.add(c);
		}
		
		if (oneArgsConstructors.isEmpty())
		{
			String possibleError = "No object-arg constructors found (not even default ones) for class ["
					+ targetClass.getCanonicalName() + "]. "
					+ "It is possible that there is not any constructor that take 1 Object argument "
					+ "or that the class is itself a subclass of another class. This will not work.";
			throw new BeanProcessorException(possibleError);
		}
		if (oneArgsConstructors.size() > 1)
		{
			throw new BeanProcessorException("More than one object-arg constructor for class [" + targetClass.getCanonicalName() + "].");
		}
		
		return oneArgsConstructors.get(0);
	}
	
}
