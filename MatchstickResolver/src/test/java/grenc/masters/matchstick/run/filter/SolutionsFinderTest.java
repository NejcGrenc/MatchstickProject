package grenc.masters.matchstick.run.filter;

import java.util.List;

import org.junit.Test;

import grenc.masters.matchstick.confirm.parse.Parser;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.run.filter.SolutionsFinder;
import grenc.masters.matchstick.writer.Writer;

public class SolutionsFinderTest
{

	@Test
	public void shouldFindAllChanges()
	{
		Equation originalEquation = new Parser().parseEquation("6-0=0");
		
		List<EquationChangeSingle> ecsList = new SolutionsFinder().getAllCorrectFinalEquations(originalEquation);
		
		new Writer().makeNewToConsole().writeList(ecsList);
	}
}