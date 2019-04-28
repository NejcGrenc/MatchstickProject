package grenc.masters.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import grenc.masters.cache.CacheProxy;
import grenc.masters.cache.annotation.Cached;
import grenc.masters.cache.annotation.ResetCache;
import grenc.masters.simplebean.Beans;
import grenc.masters.simplebean.annotation.Bean;
import grenc.masters.simplebean.annotation.InsertBean;
import grenc.masters.simplebean.processor.BeanProcessor;
import grenc.masters.simplebean.proxy.annotation.ProxyBean;


public class CachedBeanIntegrationTest
{
	private IntegrationTargetClass subject;
	
	@Before
	public void setup()
	{
		BeanProcessor.processPath("grenc.masters.integration");
		subject = Beans.get(IntegrationTargetClass.class);
	}
	
	@Test
	public void shouldImplementCace()
	{
		assertEquals(1, subject.instance.returnAndIncrement());
		assertEquals(1, subject.instance.returnAndIncrement());
		
		assertEquals(2, subject.instance.returnAndIncrementUncached());
		assertEquals(3, subject.instance.returnAndIncrementUncached());
		assertEquals(1, subject.instance.returnAndIncrement());

		subject.instance.resetCache();
		
		assertEquals(4, subject.instance.returnAndIncrementUncached());
		assertEquals(5, subject.instance.returnAndIncrement());
		assertEquals(5, subject.instance.returnAndIncrement());
		assertEquals(6, subject.instance.returnAndIncrementUncached());
	}
	
}


@Bean
class IntegrationTargetClass
{
	@InsertBean
	public IntegrationBeanClass instance;
}

@Bean
class IntegrationBeanClassImpl implements IntegrationBeanClass
{
	int value = 1;
	
	@Cached
	@Override
	public int returnAndIncrement()
	{
		return value++;
	}

	@ResetCache
	@Override
	public void resetCache() {}

	@Override
	public int returnAndIncrementUncached()
	{
		return value++;
	}
}

@ProxyBean(implementedInterface = IntegrationBeanClass.class, originalClass = IntegrationBeanClassImpl.class)
class CacheProxyBean extends CacheProxy<IntegrationBeanClass>
{
	public CacheProxyBean(Object originalInstance)
	{
		super((IntegrationBeanClass) originalInstance);
	}
}

interface IntegrationBeanClass
{
	int returnAndIncrement();
	int returnAndIncrementUncached();
	void resetCache();
}

