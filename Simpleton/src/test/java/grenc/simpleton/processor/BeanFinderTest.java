package grenc.simpleton.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import grenc.simpleton.Beans;
import grenc.simpleton.processor.exception.BeanProcessorException;


public class BeanFinderTest
{
	@Before
	public void cleanup()
	{
		Beans.removeAllBeans();
	}
	
	@Test
	public void shouldFindValidBeanForEverySupertype() throws NoSuchFieldException, SecurityException
	{
		BeanFinderComlex bean = BeanProcessor.createBean(BeanFinderComlex.class);
		Beans.registerBean(bean);
		
		assertEquals(bean, BeanFinder.findBeanForType(BeanFinderComlex.class));
		assertEquals(bean, BeanFinder.findBeanForType(BeanFinderAbstract.class));
		assertEquals(bean, BeanFinder.findBeanForType(BeanFinderInterface.class));
	}
	
	@Test(expected = BeanProcessorException.class)
	public void shouldFailToFindIfNoApplicableBeanRegistered() throws NoSuchFieldException, SecurityException
	{
		Beans.registerBean(BeanProcessor.createBean(BeanFinderSimple.class));
		
		BeanFinder.findBeanForType(BeanFinderAbstract.class);
		fail();
	}
	
	@Test(expected = BeanProcessorException.class)
	public void shouldFailToFindIfMultipleApplicableBeanRegistered() throws NoSuchFieldException, SecurityException
	{
		Beans.registerBean(BeanProcessor.createBean(BeanFinderSimple.class));
		Beans.registerBean(BeanProcessor.createBean(BeanFinderComlex.class));
		
		BeanFinder.findBeanForType(BeanFinderInterface.class);
		fail();
	}
}


interface BeanFinderInterface {}

class BeanFinderSimple implements BeanFinderInterface {}

abstract class BeanFinderAbstract {}

class BeanFinderComlex extends BeanFinderAbstract implements BeanFinderInterface {}
