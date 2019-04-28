package grenc.simpleton.scanner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import grenc.simpleton.annotation.InsertBean;

public class InsertBeanScanner
{	
	private Map<Class<?>, Field[]> annotatedFields;
	
	private InsertBeanScanner(Map<Class<?>, Field[]> annotatedFields)
	{
		this.annotatedFields = annotatedFields;
	}
	
	
	public static InsertBeanScanner scanPackage(String packageName) throws ClassNotFoundException, IOException
	{
		Class<?>[] allClasses = ClassLoader.getClasses(packageName);
		Class<?>[] annotatedClasses = ScannerUtils.containsAnnotatedFields(allClasses, InsertBean.class);
		
		Map<Class<?>, Field[]> annotatedFieldsMap = new HashMap<>();
		for (Class<?> c : annotatedClasses)
			annotatedFieldsMap.put(c, ScannerUtils.annotatedFields(c, InsertBean.class));
		
		return new InsertBeanScanner(annotatedFieldsMap);
	}
	
	
	public Class<?>[] getClasses()
	{
		return annotatedFields.keySet().toArray(new Class<?>[0]);
	}
	
	public Field[] getFields(Class<?> c)
	{
		return annotatedFields.get(c);
	}
	
	public Field[] getAllFields()
	{
		return merge(annotatedFields.values());
	}

	
	private static Field[] merge(Collection<Field[]> col)
	{
		List<Field> all = new ArrayList<>();
		for (Field[] arr : col)
			all.addAll(Arrays.asList(arr));
		return all.toArray(new Field[0]);
	}
}
