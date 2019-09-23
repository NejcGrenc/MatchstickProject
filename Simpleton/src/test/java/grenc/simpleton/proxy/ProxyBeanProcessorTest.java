package grenc.simpleton.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.junit.Test;

import grenc.simpleton.Beans;
import grenc.simpleton.processor.exception.BeanProcessorException;
import grenc.simpleton.proxy.annotation.ProxyBean;


public class ProxyBeanProcessorTest
{

	@Test
	public void shouldCreateValidProxyBean()
	{
		OriginalTestBean originalBeanInstance = new OriginalTestBean();
		assertEquals(10, originalBeanInstance.give());
		
		TestInterface proxyBean = (TestInterface) ProxyBeanProcessor.createProxyBean(originalBeanInstance);
		assertEquals(15, proxyBean.give());
	}
	
	@Test 
	public void shouldScanAndCreateProxyBean() throws ClassNotFoundException, IOException
	{
		OriginalTestBean originalBeanInstance = new OriginalTestBean();
		Beans.registerBean(originalBeanInstance);
		
		ProxyBeanProcessor.processProxyBeans("grenc.simpleton.proxy");
		
		try {
			Beans.getExact(OriginalTestBean.class);
			fail("Bean OriginalTestBean.class is not expected to be registered, but it somehow is.");
		} catch (BeanProcessorException e) {
			// Exception is thrown in case bean is not found
		}
		
		TestInterface proxyBeanCreated = Beans.getExact(TestInterface.class);
		assertNotNull(proxyBeanCreated);
	}
	
}


interface TestInterface 
{
	int give();
}

@ProxyBean(proxyClass = ProxyTestBean.class)
class OriginalTestBean implements TestInterface
{
	@Override
	public int give() { return 10; }
}

class ProxyTestBean implements InvocationHandler 
{
	private Object originalInstance;	
	public ProxyTestBean(Object originalInstance)
	{
		this.originalInstance = originalInstance;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		int original = (int) method.invoke(originalInstance, args);
		return original + 5;
	}
}
