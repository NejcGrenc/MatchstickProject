package grenc.masters.simplebean.proxy;

import java.io.IOException;

import grenc.masters.simplebean.proxy.annotation.ProxyBean;
import grenc.masters.simplebean.scanner.ClassLoader;
import grenc.masters.simplebean.scanner.ScannerUtils;


public class ProxyBeanScanner
{
	private Class<?>[] annotatedClasses;
	
	private ProxyBeanScanner(Class<?>[] annotatedClasses)
	{
		this.annotatedClasses = annotatedClasses;
	}
	
	
	public static ProxyBeanScanner scanPackage(String packageName) throws ClassNotFoundException, IOException
	{
		Class<?>[] allClasses = ClassLoader.getClasses(packageName);
		Class<?>[] annotatedClasses = ScannerUtils.annotatedClasses(allClasses, ProxyBean.class);
		
		return new ProxyBeanScanner(annotatedClasses);
	}
	
	public Class<?>[] getClasses()
	{
		return annotatedClasses;
	}
}
