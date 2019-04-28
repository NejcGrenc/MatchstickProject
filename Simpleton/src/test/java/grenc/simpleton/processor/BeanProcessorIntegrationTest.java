package grenc.simpleton.processor;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import grenc.simpleton.Beans;
import grenc.simpleton.annotation.Bean;
import grenc.simpleton.annotation.InsertBean;


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
		BeanProcessor.processPath("grenc.simpleton.processor");;
		
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

interface IntegrationTestClassInsertable
{
	int getI();
}

abstract class IntegrationTestClassInsertableAbstract implements IntegrationTestClassInsertable {}

@Bean
class IntegrationTestClassInsertableBean extends IntegrationTestClassInsertableAbstract 
{
	public int getI() { return 5; }	
}


