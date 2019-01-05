package grenc.masters.matchstick.run.filter;

import java.util.ArrayList;
import java.util.List;

import grenc.masters.matchstick.backgroundtemplate.MatchstickComparator;
import grenc.masters.matchstick.backgroundtemplate.MatchstickNumeral;
import grenc.masters.matchstick.backgroundtemplate.MatchstickOperator;
import grenc.masters.matchstick.confirm.valid.ValidElement;
import grenc.masters.matchstick.objects.main.Element;
import grenc.masters.matchstick.objects.main.Equation;

public class AllValidEquations
{

	private static List<ValidElement[]> equationStyle;
	private int equationLength = 7;
	static
	{
		equationStyle = new ArrayList<>();
		equationStyle.add(MatchstickNumeral.getAllValidElements());
		equationStyle.add(MatchstickOperator.getAllValidElements());
		equationStyle.add(MatchstickNumeral.getAllValidElements());
		equationStyle.add(MatchstickOperator.getAllValidElements());
		equationStyle.add(MatchstickNumeral.getAllValidElements());
		equationStyle.add(MatchstickComparator.getAllValidElements());
		equationStyle.add(MatchstickNumeral.getAllValidElements());
	}
	
	
	private Integer[] currentState;
	
	public AllValidEquations ()
	{
		currentState = new Integer[equationLength];
		for (int i = 0; i < equationLength; i++)
			currentState[i] = 0;
	}
	
	
	public Equation getNext()
	{
		Equation curr = getCurrent();
		
		if (currentState != null)
		increment();
		
		return curr;
	}
	
	private Equation getCurrent()
	{
		if (currentState == null)
			return null;
		
		Element[] elements = new Element[equationLength];
		for (int i = 0; i < equationLength; i++)
			elements[i] = equationStyle.get(i)[currentState[i]].getElement();
		return new Equation(elements);
	}
	
	private void increment()
	{
		increment(0);
	}
	private void increment(int depth)
	{
		if (depth == equationLength)
		{
			// We are done
			currentState = null;
			return;
		}
			
		currentState[depth]++;
		if (currentState[depth] == equationStyle.get(depth).length)
		{
			currentState[depth] = 0;
			increment(depth+1);
		}
	}
	
}
