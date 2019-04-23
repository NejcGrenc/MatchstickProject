package grenc.masters.cache;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Proxy;

import org.junit.Test;

import grenc.masters.cache.annotation.Cached;


public class CacheProcessorTest
{
	Sample subject;
	
	@Test
	public void originalTestCall() throws Exception
	{
		subject = new SampleImpl();
		
		assertEquals(1, subject.counter());
		assertEquals(2, subject.counter());
		assertEquals(3, subject.counter());
	}
	
	@Test
	public void cacheTestCall() throws Exception
	{
		subject = setupProxyInstance();
		
		assertEquals(1, subject.counter());
		assertEquals(1, subject.counter());
		assertEquals(1, subject.counter());
	}
	
	
	private Sample setupProxyInstance()
	{
		Sample originalInstance = new SampleImpl();
		return (Sample) Proxy.newProxyInstance(
				CacheProcessorTest.class.getClassLoader(), 
				new Class[] { Sample.class }, 
				new CachedWrapper<Sample>(Sample.class, originalInstance)
				);
	}

	
	private interface Sample 
	{
		int counter();
	}
	private class SampleImpl implements Sample
	{
		int store = 0;
		@Cached
		@Override public int counter() { return ++store; }		
	}
	
}
