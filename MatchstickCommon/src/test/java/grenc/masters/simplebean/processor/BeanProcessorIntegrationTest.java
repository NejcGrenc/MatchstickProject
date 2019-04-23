package grenc.masters.simplebean.processor;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import grenc.masters.simplebean.Beans;
import grenc.masters.simplebean.annotation.Bean;
import grenc.masters.simplebean.annotation.InsertBean;


public class BeanProcessorIntegrationTest
{

	@Before
	public void cleanup()
	{
		Beans.removeAllBeans();
	}
	
	@Test
	public void shouldInsertBean()
	{
		BeanProcessor.processPath("grenc.masters.simplebean.processor");;
		
		assertTrue(((IntegrationTestClassInsertInto) Beans.get(IntegrationTestClassInsertInto.class)) != null);
		assertTrue(((IntegrationTestClassInsertInto) Beans.get(IntegrationTestClassInsertInto.class)).ins != null);
		assertTrue(((IntegrationTestClassInsertInto) Beans.get(IntegrationTestClassInsertInto.class)).ins.getI() == 5);
	}
		
}



@Bean
class IntegrationTestClassInsertInto
{
	@InsertBean
	public IntegrationTestClassInsertable ins;
}

abstract class IntegrationTestClassInsertable 
{
	abstract int getI();
}

@Bean
class IntegrationTestClassInsertableBean extends IntegrationTestClassInsertable 
{
	@Override
	int getI() { return 5; }	
}


