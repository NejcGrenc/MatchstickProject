package grenc.masters.simplebean.proxy;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import grenc.masters.simplebean.Beans;
import grenc.masters.simplebean.processor.exception.BeanProcessorException;
import grenc.masters.simplebean.proxy.annotation.ProxyBean;


public class ProxyBeanProcessor
{

	public static void processProxyBeans(String packageName) throws ClassNotFoundException, IOException
	{
		Class<?>[] proxyClasses = ProxyBeanScanner.scanPackage(packageName).getClasses();
		
		for (Class<?> proxyClass : proxyClasses)
		{
			Object originalBean = popOriginalBean(proxyClass);
			Object proxyBean = createProxyBean(proxyClass, originalBean);
			Beans.registerBeanAs(proxyBean, implementedInterface(proxyClass));
		}
	}
	
	private static Object popOriginalBean(Class<?> proxyClass)
	{
		ProxyBean proxyAnnotation = proxyClass.getAnnotation(ProxyBean.class);
		Class<?> originalClass = proxyAnnotation.originalClass();
		
		Object originalBeanInstance = Beans.get(originalClass);
		System.out.println(originalClass);
		Beans.removeBean(originalClass);
		return originalBeanInstance;
	}
	
	
	static Object createProxyBean(Class<?> proxyClass, Object originalBeanInstance)
	{
		Class<?> implementedInterface = implementedInterface(proxyClass);
		
		return Proxy.newProxyInstance(
					ProxyBeanProcessor.class.getClassLoader(), 
					new Class[] { implementedInterface }, 
					createInvocationHandler(proxyClass, originalBeanInstance)
				);
	}
	
	
	private static Class<?> implementedInterface(Class<?> proxyClass)
	{
		ProxyBean proxyAnnotation = proxyClass.getAnnotation(ProxyBean.class);
		return proxyAnnotation.implementedInterface();
	}
	
	private static InvocationHandler createInvocationHandler(Class<?> proxyClass, Object originalBeanInstance)
	{
		try
		{
			assertOneConstructorWithObjectType(proxyClass);
			
			Constructor<?> constructor = proxyClass.getDeclaredConstructor(Object.class);
			constructor.setAccessible(true);
			
			return (InvocationHandler) constructor.newInstance(originalBeanInstance);
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
			throw new BeanProcessorException("Unable to instanciate proxy bean [" + proxyClass + "].", e);
		}
	}

	private static <B> void assertOneConstructorWithObjectType(Class<B> beanClass)
	{
		int oneObjectArgConstructors = 0;
		Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
		for (Constructor<?> c : constructors)
		{
			if (c.getParameterCount() == 1 && c.getGenericParameterTypes()[0] == Object.class)
				oneObjectArgConstructors++;
		}
		
		if (oneObjectArgConstructors == 0)
		{
			String possibleError = "No object-arg constructors found (not even default ones) for class ["
					+ beanClass.getCanonicalName() + "]. "
					+ "It is possible that there is not any constructor that take 1 Object argument "
					+ "or that the class is itself a subclass of another class. This will not work.";
			throw new BeanProcessorException(possibleError);
		}
		if (oneObjectArgConstructors > 1)
		{
			throw new BeanProcessorException("More than one object-arg constructor for class [" + beanClass.getCanonicalName() + "]." );
		}
	}
	
}
