package grenc.masters.matchstick.objects.changes;

import grenc.masters.matchstick.objects.main.Equation;

public class EquationChangeSingle
{
	private Equation original;
	private Equation currChange;
	private AdvancedAction advAction;

	public EquationChangeSingle (Equation original, Equation currChange)
	{
		this.original = original;
		this.currChange = currChange;
	}
	
	public EquationChangeSingle withAdvancedAction(AdvancedAction advAction)
	{
		this.advAction = advAction;
		return this;
	}
	
	public Equation getOriginalEquation()
	{
		return original;
	}
	public Equation getChangedEquation()
	{
		return currChange;
	}
	public AdvancedAction getAdvancedAction()
	{
		return advAction;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		s += getOriginalEquation();
		s += " -- ";
		s += getAdvancedAction();
		s += " -> ";
		s += getChangedEquation();
		return s;
	}
}
