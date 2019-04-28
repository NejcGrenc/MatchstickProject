package grenc.masters.simplebean.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import grenc.masters.simplebean.annotation.Bean;
import grenc.masters.simplebean.proxy.annotation.ProxyBean;

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
		
		Class<?>[] annotatedClassesBean 	 = ScannerUtils.annotatedClasses(allClasses, Bean.class);
		Class<?>[] annotatedClassesProxyBean = ScannerUtils.annotatedClasses(allClasses, ProxyBean.class);
		Class<?>[] annotatedClasses = merge(annotatedClassesBean, annotatedClassesProxyBean);
				
		return new BeanScanner(annotatedClasses);
	}
	
	public Class<?>[] getClasses()
	{
		return annotatedClasses;
	}
	
	private static Class<?>[] merge(Class<?>[] array1, Class<?>[] array2)
	{
		List<Class<?>> all = new ArrayList<>();
		all.addAll(Arrays.asList(array1));
		all.addAll(Arrays.asList(array2));
		return all.toArray(new Class<?>[0]);
	}
}
