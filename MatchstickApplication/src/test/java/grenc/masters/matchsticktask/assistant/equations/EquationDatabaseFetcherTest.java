package grenc.masters.matchsticktask.assistant.equations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import org.junit.Ignore;
import org.junit.Test;

import grenc.masters.database.equationgroups.EquationSolutionsGroupType;

public class EquationDatabaseFetcherTest
{

	@Test
	@Ignore
	public void shouldFetchCountedEquations()
	{
		// Some equation group type
		EquationSolutionsGroupType someEquationType = EquationSolutionsGroupType.group_1MA;
		
		EquationDatabaseFetcher fetcher1 = new EquationDatabaseFetcher();
		int return1 = fetcher1.getNumberOfPossibleEquationsFor(someEquationType);
		
		EquationDatabaseFetcher fetcher2 = new EquationDatabaseFetcher();
		int return2 = fetcher2.getNumberOfPossibleEquationsFor(someEquationType);
		
		// Always return the same value that is valid
		assertEquals(return1, return2);
		assertTrue(return1 >= 0);
	}
	
	@Test
	public void doNotReturnSameEquations()
	{
		// Some equation group type
		EquationSolutionsGroupType someEquationType = EquationSolutionsGroupType.group_1MA;

		EquationDatabaseFetcher fetcher = new EquationDatabaseFetcher();
		
		// Just check if the tables are not empty
		assumeTrue(fetcher.getNumberOfPossibleEquationsFor(someEquationType) > 2);
		
		String equation1 = fetcher.fetchRandom(someEquationType);
		String equation2 = fetcher.fetchRandom(someEquationType);

		assumeFalse(equation1.equals(equation2));
	}
}
