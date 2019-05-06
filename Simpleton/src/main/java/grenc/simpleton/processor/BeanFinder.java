package grenc.simpleton.processor;

import java.util.ArrayList;
import java.util.List;

import grenc.simpleton.Beans;
import grenc.simpleton.processor.exception.BeanProcessorException;


public class BeanFinder
{
	@SuppressWarnings("unchecked")
	public static <B> B findBeanForType(Class<B> type)
	{
		List<Class<?>> applicableBeans = new ArrayList<>();
		for (Class<?> beanClass : Beans.allRegisteredTypes())
		{
			if (isApplicable(type, beanClass))
				applicableBeans.add(beanClass);
		}
			
		if (applicableBeans.isEmpty())
			throw new BeanProcessorException("No applicable bean found for type [" + type.toString() + "]");
		
		if (applicableBeans.size() > 1)
		{	
			String beans = "";
			for (Class<?> bean : applicableBeans)
				beans += bean.getName() + " ";
			throw new BeanProcessorException("Too many applicable bean found for type [" + type.toString() + "] : " + beans);
		}	
		
		return (B) Beans.getExact(applicableBeans.get(0));
	}
	
	private static boolean isApplicable(Class<?> fieldType, Class<?> beanType)
	{
		if (fieldType.equals(beanType))
			return true;
		
		boolean applicable = false;
		for (Class<?> interf : beanType.getInterfaces())
			applicable = (applicable) ? true : isApplicable(fieldType, interf);
		if (beanType.getSuperclass() != null)
			applicable = (applicable) ? true : isApplicable(fieldType, beanType.getSuperclass());
		
		return applicable;
	}
}
