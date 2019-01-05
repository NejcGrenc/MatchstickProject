package grenc.masters.matchstick.run.main;

import java.util.HashSet;
import java.util.Set;

import grenc.masters.matchstick.confirm.parse.Parser;
import grenc.masters.matchstick.confirm.valid.Validator;
import grenc.masters.matchstick.objects.changes.ElementChanges;
import grenc.masters.matchstick.objects.changes.EquationChangeSingle;
import grenc.masters.matchstick.objects.changes.EquationChanges;
import grenc.masters.matchstick.objects.main.Action;
import grenc.masters.matchstick.objects.main.Element;
import grenc.masters.matchstick.objects.main.Equation;
import grenc.masters.matchstick.writer.Writer;


public class Test 
{

	public static void main(String[] args)
	{
		//equation2();
	}
	
//	private static void equationsCombination1() 
//	{
//		long start = System.currentTimeMillis();
//		Equation eq = new Parser().parseEquation("9 -1 * 4 = 4 ");
//		EquationCombinations comb = new EquationCombinations(eq);
//		comb.calculateDefault();
//		System.out.println(comb);
//		System.out.println(System.currentTimeMillis() - start);
//	}
	/*
	private static void equation2() 
	{
		Equation eq = new Parser().parseEquation("0 -4 - 1 = 4 ");
		EquationChanges chan = new EquationChanges(eq, new Action(1, 1));
		Set<Equation> newEqations = new HashSet<>();
		Writer w = new Writer();
		w.makeNew("test");
		for (EquationChangeSingle curr = chan.findNext(); curr != null; curr = chan.findNext())
		{
			w.write(curr);
			//System.out.println(curr.getChangedEquation());
			//newEqations.add(curr);
		}
		w.close();
		//System.out.println(toStringEquation(newEqations));
	}
*/
	private static void equation1() 
	{
		Equation eq = new Parser().parseEquation("8 -1 * 4 = 4 +3 -3");
		System.out.println(eq.isCorrect());
	}

	private static void simple1()
	{
		Element od = new Parser().parseSymbol("5");
		Set<Element> s = new ElementChanges(od).findAllChanged(2, 2);
		System.out.println(Validator.getSymbol(od) + " -> " + toString(s));
	}
	
	private static String toString(Set<Element> set)
	{
		String s = "";
		for (Element el : set)
			s += Validator.getSymbol(el) + " ";
		return s;
	}
	
	private static String toStringEquation(Set<Equation> set)
	{
		String s = "";
		for (Equation eq : set)
			s += eq.toString() + "\n";
		return s;
	}

}
