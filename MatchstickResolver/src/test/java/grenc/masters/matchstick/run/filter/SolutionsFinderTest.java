package grenc.masters.matchstick.run.filter;

import java.util.List;

import org.junit.Test;

import grenc.masters.matchstick.confirm.parse.Parser;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.writer.WriterConsole;

public class SolutionsFinderTest
{

	SolutionGroup unimportantGroup = SolutionGroup.group_1MA;
	
	@Test
	public void shouldFindAllChanges()
	{
		Equation originalEquation = new Parser().parseEquation("6-0=0");
		
		List<EquationChangeSingle> ecsList = new SolutionsFinder().getAllCorrectFinalEquations(originalEquation);
		
		new WriterConsole().forGroup(unimportantGroup).makeNew().writeList(ecsList);
	}
}
