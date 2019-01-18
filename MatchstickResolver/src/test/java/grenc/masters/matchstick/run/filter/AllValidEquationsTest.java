package grenc.masters.matchstick.run.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import grenc.masters.matchstick.confirm.valid.ValidElement;
import grenc.masters.matchstick.objects.main.Equation;

public class AllValidEquationsTest
{

	@Test
	public void allEquations()
	{
		// prepare: equation combinations
		List<ValidElement[]> equationOptions = new LinkedList<>();
		equationOptions.add(new ValidElement[] {TestValid.x, TestValid.y});
		equationOptions.add(new ValidElement[] {TestValid.c});
		equationOptions.add(new ValidElement[] {TestValid.n0, TestValid.n1});
		
		// when: all equations get calculated
		List<Equation> allEq = new ArrayList<>();
		AllValidEquations ave = new AllValidEquations(equationOptions);
		while (ave.notDone())
			allEq.add(ave.getNext());
		
		// then: assert that there are indeed all of them
		assertEquals(4, allEq.size());
		assertContatinsEquationString(allEq, "x=0");
		assertContatinsEquationString(allEq, "y=0");
		assertContatinsEquationString(allEq, "x=1");
		assertContatinsEquationString(allEq, "y=1");
	}
	
	
	private void assertContatinsEquationString(List<Equation> allEq, String expected)
	{
		assertTrue(allEq.stream().map(eq -> mapEq(eq)).anyMatch(eq -> eq.equals(expected)));
	}
	
	private String mapEq(Equation eq)
	{
		return Arrays.stream(eq.getElements()).map(el -> TestValid.map(el.getDisplay())).collect(Collectors.joining(""));
	}
	
	
	static class TestValid extends ValidElement
	{
		protected TestValid(String symbol, int n)
		{
			super(symbol, n);
		}
		
		static final TestValid n0 = new TestValid("0", 0);
		static final TestValid n1 = new TestValid("1", 1);
		static final TestValid x = new TestValid("x", 2);
		static final TestValid y = new TestValid("y", 3);
		static final TestValid c = new TestValid("=", 4);
		
		static String map(int... display)
		{
			return Arrays.stream(new TestValid[] {n0, n1, x, y ,c}).filter(el -> el.getDisplay()[0] == display[0]).findFirst().get().symbol;
		}
	}
}
