package grenc.masters.simplebean;

import java.util.HashMap;
import java.util.Map;

public class Beans
{
	private static Map<Class<?>, Object> beans = new HashMap<>();
	
	public static void registerBean(Object bean)
	{
		beans.put(bean.getClass(), bean);
	}
	
	public static Object get(Class<?> type)
	{
		return beans.get(type);
	}
		
	public static void removeAllBeans()
	{
		beans = new HashMap<>();
	}
}
