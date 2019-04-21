package grenc.masters.simplebean.scanner;

import java.io.IOException;
import java.util.Arrays;

import grenc.masters.simplebean.annotation.Bean;

public class BeanScanner
{	
	private Class<?>[] annotatedClasses;
	
	private BeanScanner(Class<?>[] annotatedClasses)
	{
		this.annotatedClasses = annotatedClasses;
	}
	
	
	public static BeanScanner scanPackage(String packageName) throws ClassNotFoundException, IOException
	{
		Class<?>[] allClasses = ClassLoader.getClasses(packageName);
		Class<?>[] annotatedClasses = filterAnnotatedClasses(allClasses);
		return new BeanScanner(annotatedClasses);
	}
	
	private static Class<?>[] filterAnnotatedClasses(Class<?>[] allClasses)
	{
		return Arrays.asList(allClasses).stream().filter(c -> c.isAnnotationPresent(Bean.class)).toArray(Class<?>[]::new);			
	}
	
	
	public Class<?>[] getClasses()
	{
		return annotatedClasses;
	}
}
