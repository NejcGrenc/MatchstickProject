package grenc.simpleton.proxy;

import java.io.IOException;

import grenc.simpleton.proxy.annotation.ProxyBean;
import grenc.simpleton.scanner.ClassLoader;
import grenc.simpleton.scanner.ScannerUtils;


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
