package grenc.masters.matchstick.run.filter;

import java.util.List;

import grenc.masters.matchstick.confirm.valid.ValidElement;
import grenc.masters.matchstick.objects.main.Element;
import grenc.masters.matchstick.objects.main.Equation;


public class AllValidEquations
{
	private List<ValidElement[]> equationStyle;
	
	private Integer[] currentState;
	
	
	public AllValidEquations (List<ValidElement[]> equationStyle)
	{
		this.equationStyle = equationStyle;
		initCounters();
	}
	
	private void initCounters()
	{
		this.currentState = new Integer[equationStyle.size()];
		for (int i = 0; i < length(); i++)
			this.currentState[i] = 0;
	}
	
	private int length()
	{
		return this.currentState.length;
	}
	
	
	public boolean notDone()
	{
		return currentState != null;
	}
	
	public Equation getNext()
	{
		Equation curr = getCurrent();
		
		if (notDone())
			increment();
		
		return curr;
	}
	
	private Equation getCurrent()
	{
		if (currentState == null)
			return null;
		
		Element[] elements = new Element[length()];
		for (int i = 0; i < length(); i++)
			elements[i] = equationStyle.get(i)[currentState[i]].getElement();
		return new Equation(elements);
	}
	
	private void increment()
	{
		increment(0);
	}
	private void increment(int depth)
	{
		if (depth == length())
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
