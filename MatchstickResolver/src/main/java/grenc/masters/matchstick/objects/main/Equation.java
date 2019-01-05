package grenc.masters.matchstick.objects.main;

import grenc.masters.matchstick.confirm.correct.EquationCalculator;
import grenc.masters.matchstick.confirm.valid.Validator;

public class Equation 
{
	private Element[] elementList;
	
	public Equation (Element... elements)
	{
		this.elementList = elements;
	}

	
	public Element[] getElements()
	{
		return elementList;
	}
	
	public boolean isValid()
	{
		for (Element e : elementList)
			if (! Validator.isValid(e))
				return false;
		return true;
	}
	
	public boolean isCorrect()
	{
		if (! isValid()) 
			return false;
		return new EquationCalculator().isCorrect(this);
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Equation)) return false;
		Equation e = (Equation) o;
		return this.getElements().equals(e.getElements());
	}
	
	@Override 
	public int hashCode()
	{
		int hash = 7;
		for (Element e : getElements())
			hash = hash * 11 + e.hashCode();
		return hash;
	}

	@Override
	public String toString()
	{
		String s = "";
		for (Element e : elementList)
			s += e.toString();
		return s;
	}
}
