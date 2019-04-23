package grenc.masters.simplebean.scanner;

import java.io.IOException;

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
		Class<?>[] annotatedClasses = ScannerUtils.annotatedClasses(allClasses, Bean.class);
		return new BeanScanner(annotatedClasses);
	}
	
	public Class<?>[] getClasses()
	{
		return annotatedClasses;
	}
}
