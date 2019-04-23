package grenc.masters.simplebean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import grenc.masters.simplebean.processor.exception.BeanProcessorException;

public class Beans
{
	private static Map<Class<?>, Object> beans = new HashMap<>();
	
	// TODO: What about overwriting registered beans?
	public static void registerBean(Object bean)
	{
		if (beans.containsKey(bean.getClass()))
			throw new BeanProcessorException("Bean of type [" + bean.getClass() + "] already registered.");
			
		beans.put(bean.getClass(), bean);
	}
	
	public static Object get(Class<?> type)
	{
		return beans.get(type);
	}
	
	public static Set<Class<?>> allRegisteredTypes()
	{
		return beans.keySet();
	}
		
	public static void removeAllBeans()
	{
		beans = new HashMap<>();
	}
}
