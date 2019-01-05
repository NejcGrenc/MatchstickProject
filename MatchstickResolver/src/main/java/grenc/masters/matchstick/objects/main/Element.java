package grenc.masters.matchstick.objects.main;

import java.util.Arrays;

import grenc.masters.matchstick.confirm.valid.Validator;

public class Element
{
	private int[] display;
	
	public Element (int[] display)
	{
		this.display = display;
	}

	public int[] getDisplay()
	{
		return display;
	}
	public int getDisplayLength()
	{
		return display.length;
	}


	
	public Element copy()
	{
		return new Element(copyDisplay());
	}
	private int[] copyDisplay()
	{
//		int[] newDisplay = new int[getDisplayLength()];
//		for (int i = 0; i < getDisplayLength(); i++)
//			newDisplay[i] = display[i];
		int[] newDisplay = Arrays.copyOf(display, display.length);
		return newDisplay;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Element)) return false;
		Element e = (Element) o;
		if (this.display.length != e.display.length) return false;
		for (int i = 0; i < display.length; i++)
			if (this.display[i] != e.display[i])
				return false;
		return true;
	}
	
	@Override 
	public int hashCode()
	{
		int hash = 7;
		for (int d : display)
			hash = hash * 11 + d;
		return hash;
	}

	@Override
	public String toString()
	{
		if (Validator.isValid(this))
			return Validator.getSymbol(this);
		else
			return "?";
	}
}
