package grenc.masters.cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import grenc.masters.cache.annotation.Cached;


public class CachedWrapper<T> implements InvocationHandler 
{
    private final T originalInstance;
    private final Map<String, Method> allMethods;
    
    private Map<String, Method> annotatedMethods;
    
    private Map<Invocation, Object> cachedValues;

    
    public CachedWrapper(Class<T> interfaceClass, T originalInstance)
	{
		this.originalInstance = originalInstance;
		
		this.allMethods = new HashMap<>();
		this.annotatedMethods = new HashMap<>();
		this.cachedValues = new HashMap<>();
		
		Method[] methods = merge(originalInstance.getClass().getMethods(), originalInstance.getClass().getDeclaredMethods());
        for (Method method : methods)
        {
        	allMethods.put(method.getName(), method);
    		if (method.isAnnotationPresent(Cached.class))
    			annotatedMethods.put(method.getName(), method);
        }
	}

 
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
    {
    	System.out.println("Calling method: " + method.getName());
    	System.out.println("Will use proxy: " + shouldUseCache(method));

    	if (shouldUseCache(method))
    	{
    		Object cachedValue = cachedValues.get(new Invocation(proxy, method, args));
    		if (cachedValue != null)
    		{
    			System.out.println("Returning cached value: " + cachedValue);
    			return cachedValue;
    		}
    	}
    	
        Object result = allMethods.get(method.getName()).invoke(originalInstance, args);
        
    	if (shouldUseCache(method))
    	{
			System.out.println("Saving cached value: " + result);
    		cachedValues.put(new Invocation(proxy, method, args), result);
    	}
        
        return result;
    }
    
    private boolean shouldUseCache(Method method)
    {
    	return annotatedMethods.containsKey(method.getName());
    }
    
	
	private static Method[] merge(Method[] array1, Method[] array2)
	{
		List<Method> all = new ArrayList<>();
		all.addAll(Arrays.asList(array1));
		all.addAll(Arrays.asList(array2));
		return all.toArray(new Method[0]);
	}
    
    
    
    private class Invocation
    {
    	private Object proxy;
    	private Method method;
    	private Object[] args;
    	
    	Invocation(Object proxy, Method method, Object[] args)
    	{
    		this.proxy = proxy;
    		this.method = method;
    		this.args = args;
    		System.out.println("Created new Invocation: "+proxy + " "+method+" "+args);
    	}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(args);
			result = prime * result + ((method == null) ? 0 : method.hashCode());
			result = prime * result + ((proxy == null) ? 0 : proxy.hashCode());
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			
			if (obj == null || ! (obj instanceof CachedWrapper.Invocation))
				return false;

			Invocation other = (Invocation) obj;

			if (! Arrays.equals(args, other.args))
				return false;
			
			if (! Objects.equals(method, other.method))
				return false;
			
			if (! Objects.equals(method, other.method))
				return false;
			
			return true;
		}

    }
}

