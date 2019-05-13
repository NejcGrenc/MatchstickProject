package grenc.masters.matchstick.run.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import grenc.masters.matchstick.confirm.parse.Parser;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.main.Equation;


public class SolutionLinearOperatorOrderFilterTest
{
	private Parser parser = new Parser();
	private SolutionLinearOperatorOrderFilter filter = new SolutionLinearOperatorOrderFilter();
	
	private List<EquationChangeSingle> originalList = Arrays.asList(
			makeSolution("1+1+1=2"),
			makeSolution("1*1+1=2"),
			makeSolution("1+1*1=2"),
			makeSolution("1*1*1=2"),
			makeSolution("1-1-1=2"),
			makeSolution("1/1-1=2"),
			makeSolution("1-1/1=2"),
			makeSolution("1/1-1=2")
			);
	private List<EquationChangeSingle> expectedList = Arrays.asList(
			makeSolution("1+1+1=2"),
			makeSolution("1*1+1=2"),
			makeSolution("1*1*1=2"),
			makeSolution("1-1-1=2"),
			makeSolution("1/1-1=2"),
			makeSolution("1/1-1=2")
			);	
	
	
	@Test
	public void shouldFilterValidSolutions()
	{
		List<EquationChangeSingle> resultList = filter.filter(originalList);
		assertListsEqual(expectedList, resultList);
	}
	
	@Test
	public void shouldSelectFlawedGroups()
	{
		assertTrue(filter.isFlawed(originalList));
		assertFalse(filter.isFlawed(expectedList));
	}
	
	private EquationChangeSingle makeSolution(String solution)
	{
		Equation originalEqDummy = parser.parseEquation("1+1=2");
		Equation solutionEq = parser.parseEquation(solution);
		return new EquationChangeSingle(originalEqDummy, solutionEq);
	}
	
	private void assertListsEqual(List<EquationChangeSingle> list1, List<EquationChangeSingle> list2)
	{
		assertEquals(list1.size(), list2.size());
		for (EquationChangeSingle eq : list1)
			assertTrue(list2.contains(eq));
	}
	
}
