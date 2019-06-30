package grenc.masters.matchsticktask.assistant;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import grenc.masters.database.equationgroups.EquationSolutionsGroupType;
import grenc.masters.matchsticktask.assistant.equations.EquationDatabaseFetcher;

public class EquationAssistTest
{

	@Ignore
	@Test
	public void findUnused()
	{
		EquationSolutionsGroupType someEquationType = EquationSolutionsGroupType.group_1MA;
		
		// Return a predefined list of equations
		EquationDatabaseFetcher fetcher = Mockito.mock(EquationDatabaseFetcher.class);
		Mockito.when(fetcher.fetchRandom(someEquationType)).thenReturn("1+1=2", "1+2=3", "1+3=4");
		
		// and use it in a mocked assistant
		
		// FIXME!
		EquationAssist assistant = Mockito.mock(EquationAssist.class);
		//new EquationAssist(null, null, fetcher);
		
		// Prepare: Used equations
		List<String> usedEquations = Arrays.asList("1+2=3", "1+1=2");

		// Fetch used
//		String newUnusedEq = assistant.findUnusedEquiation(someEquationType, usedEquations);
//		
//		assertEquals("1+3=4", newUnusedEq);
	}

}
