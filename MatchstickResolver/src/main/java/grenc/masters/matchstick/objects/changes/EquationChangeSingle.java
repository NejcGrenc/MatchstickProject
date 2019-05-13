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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((advAction == null) ? 0 : advAction.hashCode());
		result = prime * result + ((currChange == null) ? 0 : currChange.hashCode());
		result = prime * result + ((original == null) ? 0 : original.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EquationChangeSingle other = (EquationChangeSingle) obj;
		if (advAction == null)
		{
			if (other.advAction != null)
				return false;
		} else if (!advAction.equals(other.advAction))
			return false;
		if (currChange == null)
		{
			if (other.currChange != null)
				return false;
		} else if (!currChange.equals(other.currChange))
			return false;
		if (original == null)
		{
			if (other.original != null)
				return false;
		} else if (!original.equals(other.original))
			return false;
		return true;
	}
	
	
}
