package grenc.masters.cache;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Proxy;

import org.junit.Test;

import grenc.masters.cache.annotation.Cached;
import grenc.masters.cache.annotation.ResetCache;


public class CacheProxyTest
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
	
	@Test
	public void cacheTestCallWithOtherIncreases() throws Exception
	{
		subject = setupProxyInstance();
		
		assertEquals(1, subject.counter());
		assertEquals(2, subject.regularCall());
		assertEquals(1, subject.counter());
		assertEquals(3, subject.regularCall());
		assertEquals(1, subject.counter());
		assertEquals(4, subject.regularCall());
	}
	
	@Test
	public void cacheTestUncachedCall() throws Exception
	{
		subject = setupProxyInstance();
		
		assertEquals(1, subject.regularCall());
		assertEquals(2, subject.regularCall());
		assertEquals(3, subject.regularCall());
	}
	
	@Test
	public void cacheTestWithResetCacheCall() throws Exception
	{
		subject = setupProxyInstance();
		
		// Cached call
		subject.regularCall();  // 1
		assertEquals(2, subject.counter());  // 2
		assertEquals(2, subject.counter());
		subject.regularCall();  // 3
		assertEquals(2, subject.counter());
		
		subject.resetCounter(0);
		
		// Cached call again
		assertEquals(1, subject.counter());  // 1
		assertEquals(1, subject.counter());
	}
	
	
	private Sample setupProxyInstance()
	{
		Sample originalInstance = new SampleImpl();
		return (Sample) Proxy.newProxyInstance(
				this.getClass().getClassLoader(), 
				new Class[] { Sample.class }, 
				new CacheProxy<Sample>(originalInstance)
				);
	}

	
	private interface Sample 
	{
		int counter();
		int resetCounter(int val);
		int regularCall();
	}
	private class SampleImpl implements Sample
	{
		int store = 0;
		
		@Cached
		@Override public int counter() { return ++store; }
		
		@ResetCache
		@Override public int resetCounter(int val) { store = val; return 0; }

		@Override
		public int regularCall() { return ++store; }	
	}
	
}
