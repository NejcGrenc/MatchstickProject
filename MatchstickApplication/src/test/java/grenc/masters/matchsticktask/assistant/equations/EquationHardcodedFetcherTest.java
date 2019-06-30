package grenc.masters.matchsticktask.assistant.equations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import grenc.masters.database.equationgroups.EquationSolutionsGroupType;

public class EquationHardcodedFetcherTest
{
	private EquationHardcodedFetcher fetcher = new EquationHardcodedFetcher();
	
	@Test
	public void getEquation()
	{
		assertEquals("2/2*3=2", fetcher.equationForGroupAndTaskNo(EquationSolutionsGroupType.group_1N, 2));
	}
	
	@Test (expected = RuntimeException.class)
	public void invalidGroupType()
	{
		fetcher.equationForGroupAndTaskNo(EquationSolutionsGroupType.group_1N_2N_3N, 2);
	}
	
	@Test (expected = RuntimeException.class)
	public void invalidTaskNumber()
	{
		fetcher.equationForGroupAndTaskNo(EquationSolutionsGroupType.group_1N, 5);
	}
}
