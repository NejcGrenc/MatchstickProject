package grenc.masters.matchstick.run.filter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import grenc.masters.matchstick.confirm.parse.Parser;
import grenc.masters.matchstick.objects.changes.AdvancedAction;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.main.Action;
import grenc.masters.matchstick.objects.main.Equation;

public class GroupSelectorTest
{
	@Test
	public void shouldFindAllChanges()
	{
		Equation originalEquation = new Parser().parseEquation("6-0=0");
		
		List<EquationChangeSingle> ecsList = new SolutionsFinder().getAllCorrectFinalEquations(originalEquation);
		
		SolutionGroup group = new GroupSelector(ecsList).findGroup();
		
		System.out.println(new GroupSelector(ecsList).parseForMoves(1));
		System.out.println(new GroupSelector(ecsList).parseForMoves(2));
		System.out.println(new GroupSelector(ecsList).parseForMoves(3));
		
		System.out.println(group);
	}
	
	@Test
	public void properlyAssignGroupFor_OneNumeralMove()
	{
		EquationChangeSingle change1 = create("6-0=0", "0-0=0", action(1).matchMovedWithinNumeral());
		EquationChangeSingle change2 = create("6-0=0", "6-6=0", action(1).matchMovedWithinNumeral());
		EquationChangeSingle change3 = create("6-0=0", "6-0=6", action(1).matchMovedWithinNumeral());
		List<EquationChangeSingle> ecsList = Arrays.asList(change1, change2, change3);
		
		SolutionGroup group = new GroupSelector(ecsList).findGroup();
		assertEquals(SolutionGroup.group_1N, group);
	}
	
	@Test
	public void properlyAssignGroupFor_TwoNumeralMoves()
	{
		EquationChangeSingle change11 = create("6-0=0", "0-0=0", action(1).matchMovedWithinNumeral());
		EquationChangeSingle change12 = create("6-0=0", "6-6=0", action(1).matchMovedWithinNumeral());

		EquationChangeSingle change21 = create("6-0=0", "9-9=0", action(2).matchMovedWithinNumeral().matchMovedWithinNumeral());
		EquationChangeSingle change22 = create("6-0=0", "6-6=0", action(2).matchMovedWithinNumeral().matchMovedWithinNumeral());
		
		List<EquationChangeSingle> ecsList = Arrays.asList(change11, change12, change21, change22);
		
		SolutionGroup group = new GroupSelector(ecsList).findGroup();
		assertEquals(SolutionGroup.group_1N_2N, group);
	}

	@Test
	public void properlyAssignGroupFor_OneNumeralAndTwoMixedMoves()
	{
		EquationChangeSingle change11 = create("6-0=0", "0-0=0", action(1).matchMovedWithinNumeral());
		EquationChangeSingle change12 = create("6-0=0", "6-6=0", action(1).matchMovedWithinNumeral());

		EquationChangeSingle change21 = create("6-0=0", "9-9=0", action(2).matchMovedWithinNumeral().matchMovedWithinNumeral());
		EquationChangeSingle change22 = create("6-0=0", "5*0=0", action(2).matchMovedWithinNumeral().matchRemovedFromNumeral().matchAddedToOperator());
		
		List<EquationChangeSingle> ecsList = Arrays.asList(change11, change12, change21, change22);
		
		SolutionGroup group = new GroupSelector(ecsList).findGroup();
		assertEquals(SolutionGroup.group_1N, group);
	}
	
	@Test
	public void properlyAssignGroupFor_OneNumeralAndTwoMixedMovesNoWithin()
	{
		EquationChangeSingle change11 = create("6-0=0", "0-0=0", action(1).matchMovedWithinNumeral());

		EquationChangeSingle change21 = create("6-0=0", "8-8-0=0", action(2).matchRemovedFromOperator().matchRemovedFromOperator()
																			.matchAddedToNumeral().matchAddedToNumeral());
		
		List<EquationChangeSingle> ecsList = Arrays.asList(change11, change21);
		
		SolutionGroup group = new GroupSelector(ecsList).findGroup();
		assertEquals(SolutionGroup.group_1N, group);
	}
	
	@Test 
	public void properlyAssignGroupFor_NotSolvableInOneMove()
	{
		// Equations with only one move missing

		EquationChangeSingle change21 = create("6-0=0", "9-9=0", action(2).matchMovedWithinNumeral().matchMovedWithinNumeral());
		EquationChangeSingle change22 = create("6-0=0", "6-6=0", action(2).matchMovedWithinNumeral().matchMovedWithinNumeral());
		
		List<EquationChangeSingle> ecsList = Arrays.asList(change21, change22);
		
		SolutionGroup group = new GroupSelector(ecsList).findGroup();
		assertEquals(SolutionGroup.group_1X_2N, group);
	}
	
	@Test
	public void properlyAssignNoGroupFor_NotSolvableInOneOrTwoMoves()
	{
		List<EquationChangeSingle> ecsList = Collections.emptyList();
		
		SolutionGroup group = new GroupSelector(ecsList).findGroup();
		assertEquals(null, group);
	}
	
	
	private EquationChangeSingle create(String original, String solution, AdvancedAction advAction)
	{
		Equation originalEquation = new Parser().parseEquation(original);
		Equation solutionEquation = new Parser().parseEquation(solution);
		return new EquationChangeSingle(originalEquation, solutionEquation).withAdvancedAction(advAction);
	}
	
	private AdvancedAction action(int moves)
	{
		return new AdvancedAction(new Action(moves));
	}
	
}
