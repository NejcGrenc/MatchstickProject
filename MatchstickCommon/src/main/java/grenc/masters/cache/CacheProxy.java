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
import grenc.masters.cache.annotation.ResetCache;


public class CacheProxy<T> implements InvocationHandler 
{
    private final T originalInstance;
    private List<String> annotatedMethodsCached;
    private List<String> annotatedMethodsResetCache;

    private Map<Invocation, Object> cachedValues;

    
    public CacheProxy(T originalInstance)
	{
		this.originalInstance = originalInstance;
		
		this.annotatedMethodsCached = new ArrayList<>();
		this.annotatedMethodsResetCache = new ArrayList<>();

		this.cachedValues = new HashMap<>();
		
		Method[] methods = merge(originalInstance.getClass().getMethods(), originalInstance.getClass().getDeclaredMethods());
        for (Method method : methods)
        {
    		if (method.isAnnotationPresent(Cached.class))
    			annotatedMethodsCached.add(method.getName());
    		else if (method.isAnnotationPresent(ResetCache.class))
    			annotatedMethodsResetCache.add(method.getName());
        }
	}

 
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable 
    {
    	if (shouldUseCache(method))
    	{
    		Object cachedValue = cachedValues.get(new Invocation(proxy, method, args));
    		if (cachedValue != null)
    		{
    			return cachedValue;
    		}
    	}
    	if (shouldResetCache(method))
    	{
    		cachedValues = new HashMap<>();
    	}
    	
    	method.setAccessible(true);
        Object result = method.invoke(originalInstance, args);

        
    	if (shouldUseCache(method))
    	{
    		cachedValues.put(new Invocation(proxy, method, args), result);
    	}
        
        return result;
    }
    
    private boolean shouldUseCache(Method method)
    {
    	return annotatedMethodsCached.contains(method.getName());
    }
    
    private boolean shouldResetCache(Method method)
    {
    	return annotatedMethodsResetCache.contains(method.getName());
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
			
			if (obj == null || ! (obj instanceof CacheProxy.Invocation))
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

