package grenc.simpleton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import grenc.simpleton.processor.BeanFinder;
import grenc.simpleton.processor.exception.BeanProcessorException;

public class Beans
{
	private static Map<Class<?>, Object> beans = new HashMap<>();
	
	public static void registerBean(Object bean)
	{
		registerBeanAs(bean, bean.getClass());
	}
	
	public static void registerBeanAs(Object bean, Class<?> type)
	{
		if (beans.containsKey(type))
			throw new BeanProcessorException("Bean of exact type [" + bean.getClass() + "] already registered.");
			
		beans.put(type, bean);
	}
	
	
	/* Removing a bean doesn't remove it from already inserted places */
	public static void removeBean(Class<?> originalBean)
	{
		beans.remove(originalBean);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getExact(Class<T> type)
	{
		return (T) beans.get(type);
	}
	
	public static <T> T get(Class<T> type)
	{
		return BeanFinder.findBeanForType(type);
	}
	
	public static Set<Class<?>> allRegisteredTypes()
	{
		return beans.keySet();
	}
	
	public static Collection<Object> allRegisteredBeans()
	{
		return beans.values();
	}
		
	public static void removeAllBeans()
	{
		beans = new HashMap<>();
	}
}
